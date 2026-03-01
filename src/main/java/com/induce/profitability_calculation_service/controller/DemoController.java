package com.induce.profitability_calculation_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.induce.profitability_calculation_service.model.User;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping
    @Operation(summary = "Тестовый метод с приветствием")
    public ResponseEntity<String> sayHello(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok("Привет, " + user.getEmail() + "! Твой ID в базе: " + user.getId());
    }
}
