package com.induce.profitability_calculation_service.dto;

import java.math.BigDecimal;
import java.util.List;

import com.induce.profitability_calculation_service.model.FinancialFrequency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepositResponse {
  private BigDecimal finalAmount;
  private BigDecimal accruedInterest;
  private BigDecimal effectiveRate;
  private FinancialFrequency frequency;
  private List<BigDecimal> capitalGrowthGraph;
}
