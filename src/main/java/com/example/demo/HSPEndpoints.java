package com.example.demo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HSPEndpoints {

    private Logger LOG = LoggerFactory.getLogger(getClass());

    private Map<String, String> response = new HashMap<>();

    @PostConstruct
    private void init() {
        response.put("status", "0");
        response.put("usernumma", "user");
        response.put("owner", "Gipsz Jakab");
        response.put("kifesto", "kifesto");
        response.put("mazolo", "_____FoxDecryptionPassword;)___________");
    }
    
    @GetMapping("/Engine/Preloader")
    public ResponseEntity<Map<String, String>> method1(HttpServletRequest request) {
        printRequestInfo(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/Checklua/Serlod")
    public ResponseEntity<Map<String, String>> method2(HttpServletRequest request) {
        printRequestInfo(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/Checklua/Serial")
    public ResponseEntity<Map<String, String>> method3(HttpServletRequest request,
                                                       @RequestParam("Addcp") String cpuid,
                                                       @RequestParam("Addhd") String diskid,
                                                       @RequestParam("Partid") String partid,
                                                       @RequestParam("Bdsn") String boardid) {
        printRequestInfo(request);
        response.put("status", "1");
        response.put("cpuid", cpuid);
        response.put("diskid", diskid);
        response.put("diskpart", partid);
        response.put("boardid", boardid);
        return new ResponseEntity<>(Collections.emptyMap(), HttpStatus.OK);
    }

    @GetMapping("/Checklua/Gipszelo")
    public ResponseEntity<Map<String, String>> method4(HttpServletRequest request) {
        printRequestInfo(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void printRequestInfo(HttpServletRequest request) {
        String authHeader = "authorization";
        LOG.info("{} | {}: {}", request.getRequestURL(), authHeader, request.getHeader(authHeader));
    }
}
