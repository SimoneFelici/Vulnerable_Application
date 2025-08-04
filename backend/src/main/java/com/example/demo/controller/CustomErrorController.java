package com.example.demo.controller;

import com.example.demo.config.VulnerabilityConfig;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

// Import da jakarta invece di javax
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    private final VulnerabilityConfig vulnerabilityConfig;

    public CustomErrorController(VulnerabilityConfig vulnerabilityConfig) {
        this.vulnerabilityConfig = vulnerabilityConfig;
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {

                boolean xssEnabled = vulnerabilityConfig.isEnabled("XSS_REFLECTED");
                model.addAttribute("xssEnabled", xssEnabled);


                return "error/404";
            }
        }


        return "error/genericError";
    }

}
