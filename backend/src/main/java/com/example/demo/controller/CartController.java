package com.example.demo.controller;

import com.example.demo.config.VulnerabilityConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CartController {

    private static final List<String> cartItems = new ArrayList<>();

    @Autowired
    private VulnerabilityConfig vulnerabilityConfig;

    @PostMapping("/api/cart")
    public ResponseEntity<String> processCart(@RequestBody String xmlPayload) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            if (!vulnerabilityConfig.isEnabled("XEE")) {
                factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
                factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
                factory.setXIncludeAware(false);
                factory.setExpandEntityReferences(false);
            }
            // Se vulnerability.features.XEE è true, il parser rimane vulnerabile a XXE

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlPayload)));

            NodeList items = doc.getElementsByTagName("item");
            for (int i = 0; i < items.getLength(); i++) {
                Node item = items.item(i);
                String fruit = item.getTextContent().trim();
                if (!fruit.isEmpty()) {

                    if (!vulnerabilityConfig.isEnabled("XSS_STORED")) {
                        fruit = Encode.forHtml(fruit);
                    }
                    cartItems.add(fruit);
                }
            }
            return ResponseEntity.ok("Carrello aggiornato: " + cartItems.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("XML non valido");
        }
    }

    @GetMapping("/api/cart")
    public ResponseEntity<List<String>> getCart() {
        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/api/cart/checkout")
    public ResponseEntity<String> checkout() {
        if (cartItems.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Il carrello è vuoto.");
        }
        cartItems.clear();
        return ResponseEntity.ok("Acquisto completato con successo!");
    }

}