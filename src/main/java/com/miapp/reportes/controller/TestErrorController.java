package com.miapp.reportes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestErrorController {

    @GetMapping("/test-error")
    public String testError() {
        throw new RuntimeException("Test error for 500 page.");
    }
}
