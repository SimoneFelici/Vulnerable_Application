package com.example.demo.controller;

import com.example.demo.config.VulnerabilityConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class DirectoryListingController {

    private final ResourcePatternResolver resourcePatternResolver;
    private final VulnerabilityConfig vulnerabilityConfig;

    public DirectoryListingController(
            ResourcePatternResolver resourcePatternResolver,
            VulnerabilityConfig vulnerabilityConfig) {
        this.resourcePatternResolver = resourcePatternResolver;
        this.vulnerabilityConfig = vulnerabilityConfig;
    }

    @GetMapping(value = {"/images", "/images/"}, produces = "text/html")
    @ResponseBody
    public String listImages() throws IOException {
        if (!vulnerabilityConfig.isEnabled("DIRECTORY_LISTING")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
        }

        Resource[] resources = resourcePatternResolver.getResources("classpath*:static/images/**/*.png");

        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Index of /images/</title>");
        html.append("<style>");
        html.append(".listing { font-family: monospace; }");
        html.append(".name { width: 10%; display: inline-block; }");
        html.append(".date { width: 10%; display: inline-block; }");
        html.append(".size { width: 5%; display: inline-block; text-align: right; }");
        html.append("</style></head><body>");
        html.append("<h1>Index of /images/</h1><hr><div class='listing'>");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");

        for (Resource resource : resources) {
            String uriString = resource.getURI().toString();
            int staticIndex = uriString.indexOf("static/images/");

            if (staticIndex == -1) continue;

            String relativePath = uriString.substring(staticIndex + "static/images/".length())
                    .replace("\\", "/");
            String fileHref = "/images/" + relativePath;
            String fileName = relativePath;
            String lastModified = dateFormat.format(new Date(resource.lastModified()));
            String size = formatSize(resource.contentLength());

            html.append("<div>");
            html.append("<span class='name'><a href='").append(fileHref).append("' target=\"_blank\">").append(fileName).append("</a></span>");
            html.append("<span class='date'>").append(lastModified).append("</span>");
            html.append("<span class='size'>").append(size).append("</span>");
            html.append("</div>");
        }

        html.append("</div><hr></body></html>");
        return html.toString();
    }

    private String formatSize(long bytes) {
        if (bytes <= 0) return "-";
        if (bytes < 1024) return bytes + " B";
        return String.format("%,.1f KB", bytes / 1024.0);
    }
}