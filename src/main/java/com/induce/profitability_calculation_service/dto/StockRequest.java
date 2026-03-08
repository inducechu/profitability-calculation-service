package com.induce.profitability_calculation_service.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockRequest {
  @NotNull(message = "Purchase price is required")
  private BigDecimal purchasePrice;

  @NotNull(message = "Current price is required")
  private BigDecimal currentPrice;

  @NotNull(message = "Holding period is required")
  private Integer holdingMonths;

  @NotNull(message = "Dividend rate is required")
  private BigDecimal dividendRate;

  @NotBlank(message = "Dividend frequency is required")
  private String dividendFrequency;

  @NotNull(message = "Commission is required")
  private BigDecimal commission;
}
