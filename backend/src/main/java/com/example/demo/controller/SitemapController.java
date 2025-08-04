package com.example.demo.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SitemapController {

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String getBackendSitemap() {
        return """
            <?xml version="1.0" encoding="UTF-8"?>
            <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
                <url>
                    <loc>http://localhost:8081/api/products</loc>
                    <lastmod>2024-01-01</lastmod>
                </url>
                <url>
                    <loc>http://localhost:8081/api/login</loc>
                    <lastmod>2024-01-01</lastmod>
                </url>
                <url>
                    <loc>http://localhost:8081/images/</loc>
                    <lastmod>2024-01-01</lastmod>
                </url>
            </urlset>
            """;
    }

    @GetMapping(value = "/sitemapindex.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String getSitemapIndex() {
        return """
            <?xml version="1.0" encoding="UTF-8"?>
            <sitemapindex xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
                <sitemap>
                    <loc>http://localhost:8080/sitemap.xml</loc>
                    <lastmod>2024-01-01</lastmod>
                </sitemap>
                <sitemap>
                    <loc>http://localhost:8081/sitemap.xml</loc>
                    <lastmod>2024-01-01</lastmod>
                </sitemap>
            </sitemapindex>
            """;
    }
}