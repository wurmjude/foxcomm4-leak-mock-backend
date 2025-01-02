package com.example.demo.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.License;
import com.example.demo.service.LicensingService;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class LicensingController {

    private Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private LicensingService licensingService;

    @PostConstruct
    private void init() {
        licensingService.addLicense("serial", "token", "user", "Gipsz Jakab");
    }

    @PostMapping("add_license")
    public ResponseEntity<String> addLicense(HttpServletRequest request,
                           @RequestParam("serial") String serial,
                           @RequestParam("magic_key") String magicKey,
                           @RequestParam("user_id") String userID,
                           @RequestParam("full_name") String fullName) {
        try {
            licensingService.addLicense(serial, magicKey, userID, fullName);
            return ResponseEntity.ok("License added");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Hidden
    @GetMapping("/Engine/Preloader")
    public ResponseEntity<License> getLicenseByBearerToken(HttpServletRequest request, @RequestParam Map<String, String> params) {
        printRequestInfo(request, params);
        String token = getBearerToken(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var lic = licensingService.getLicenseByBearerToken(token);
        return ResponseEntity.ok((lic != null) ? lic : new License());
    }

    @Hidden
    @GetMapping("/Checklua/Gipszelo")
    public ResponseEntity<License> getLicenseByMagicKey(HttpServletRequest request, @RequestParam Map<String, String> params) {
        printRequestInfo(request, params);
        String token = getBearerToken(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var lic = licensingService.getLicenseByMagicKey(token);
        return ResponseEntity.ok(lic);
    }

    @Hidden
    @GetMapping("/Checklua/Serlod")
    public ResponseEntity<License> validateLicense(HttpServletRequest request, @RequestParam Map<String, String> params) {
        printRequestInfo(request, params);
        String token = getBearerToken(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var licByToken = licensingService.getLicenseByMagicKey(token);
        if (licByToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var licBySerial = licensingService.getLicenseBySerial(params.get("Serl"));
        if (licBySerial == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (!licBySerial.equals(licByToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(licBySerial);
    }

    @Hidden
    @GetMapping("/Checklua/Serial")
    public ResponseEntity<License> activateLicense(HttpServletRequest request, @RequestParam Map<String, String> params) {
        printRequestInfo(request, params);
        var lic = licensingService.activateLicense(params.get("AddSer"), params.get("Addcp"), params.get("Addhd"), params.get("Partid"), params.get("Bdsn"));
        if (lic == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(lic);
    }

    private String getBearerToken(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        String prefix = "Bearer ";
        return (token != null && token.startsWith(prefix)) ? token.substring(prefix.length()) : null;
    }

    private void printRequestInfo(HttpServletRequest request, Map<String, String> params) {
        var sb = new StringBuilder();
        params.forEach((k, v) -> sb.append(String.format("    %s=%s\n", k, v)));
        LOG.info("{}\n    BearerToken={}\n{}", request.getRequestURL(), getBearerToken(request), sb.toString());
    }
}
