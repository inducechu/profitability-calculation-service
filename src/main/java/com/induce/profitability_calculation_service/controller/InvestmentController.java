package com.induce.profitability_calculation_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.induce.profitability_calculation_service.dto.BondRequest;
import com.induce.profitability_calculation_service.dto.BondResponse;
import com.induce.profitability_calculation_service.dto.DepositRequest;
import com.induce.profitability_calculation_service.dto.DepositResponse;
import com.induce.profitability_calculation_service.dto.StockRequest;
import com.induce.profitability_calculation_service.dto.StockResponse;
import com.induce.profitability_calculation_service.model.User;
import com.induce.profitability_calculation_service.service.BondService;
import com.induce.profitability_calculation_service.service.DepositService;
import com.induce.profitability_calculation_service.service.StockService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/calculate")
@RequiredArgsConstructor
public class InvestmentController {

  private final DepositService depositService;
  private final BondService bondService;
  private final StockService stockService;

  @PostMapping("/deposit")
  public ResponseEntity<DepositResponse> calculateDeposit(
      @Valid @RequestBody DepositRequest request,
      @AuthenticationPrincipal User user) {
    return ResponseEntity.ok(depositService.calculateAndSave(request, user));
  }

  @PostMapping("/bond")
  public ResponseEntity<BondResponse> calculateBond(
      @Valid @RequestBody BondRequest request,
      @AuthenticationPrincipal User user) {
    return ResponseEntity.ok(bondService.calculateAndSave(request, user));
  }

  @PostMapping("/stock")
  public ResponseEntity<StockResponse> calculateStock(
      @Valid @RequestBody StockRequest request,
      @AuthenticationPrincipal User user) {
    return ResponseEntity.ok(stockService.calculateAndSave(request, user));
  }

}
