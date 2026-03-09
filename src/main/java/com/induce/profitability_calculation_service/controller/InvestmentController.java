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
import com.induce.profitability_calculation_service.model.InvestmentType;
import com.induce.profitability_calculation_service.model.User;
import com.induce.profitability_calculation_service.service.CalculationFactory;
import com.induce.profitability_calculation_service.service.CalculationStrategy;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/calculate")
@RequiredArgsConstructor
public class InvestmentController {

  private final CalculationFactory calculationFactory;

  @PostMapping("/deposit")
  @SuppressWarnings("unchecked")
  public ResponseEntity<DepositResponse> calculateDeposit(
      @Valid @RequestBody DepositRequest request,
      @AuthenticationPrincipal User user) {
    CalculationStrategy<DepositRequest, DepositResponse> strategy = (CalculationStrategy<DepositRequest, DepositResponse>) calculationFactory
        .getStrategy(InvestmentType.DEPOSIT);
    return ResponseEntity.ok(strategy.calculateAndSave(request, user));
  }

  @PostMapping("/bond")
  @SuppressWarnings("unchecked")
  public ResponseEntity<BondResponse> calculateBond(
      @Valid @RequestBody BondRequest request,
      @AuthenticationPrincipal User user) {
    CalculationStrategy<BondRequest, BondResponse> strategy = (CalculationStrategy<BondRequest, BondResponse>) calculationFactory
        .getStrategy(InvestmentType.BOND);
    return ResponseEntity.ok(strategy.calculateAndSave(request, user));
  }

  @PostMapping("/stock")
  @SuppressWarnings("unchecked")
  public ResponseEntity<StockResponse> calculateStock(
      @Valid @RequestBody StockRequest request,
      @AuthenticationPrincipal User user) {
    CalculationStrategy<StockRequest, StockResponse> strategy = (CalculationStrategy<StockRequest, StockResponse>) calculationFactory
        .getStrategy(InvestmentType.STOCK);

    return ResponseEntity.ok(strategy.calculateAndSave(request, user));
  }

}
