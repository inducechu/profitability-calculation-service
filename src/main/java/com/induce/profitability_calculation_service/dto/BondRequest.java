package com.induce.profitability_calculation_service.dto;

import java.math.BigDecimal;

import com.induce.profitability_calculation_service.model.FinancialFrequency;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
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
public class BondRequest {

  @NotNull(message = "Nominal value is required")
  @DecimalMin(value = "0.0", inclusive = false, message = "Nominal must be greater than zero")
  private BigDecimal nominal;

  @NotNull(message = "Purchase price (%) is required")
  @DecimalMin(value = "0.0", inclusive = false, message = "Purchase price percent must be greater than zero")
  @DecimalMax(value = "200.0", message = "Purchase price percent is too high")
  private BigDecimal purchasePricePercent;

  @NotNull(message = "Coupon rate is required")
  @DecimalMin(value = "0.0", message = "Coupon rate cannot be negative")
  private BigDecimal couponRate;

  @NotNull(message = "Frequency is required")
    private FinancialFrequency frequency;

    @NotNull(message = "Term in months is required")
    @Min(1)
    private Integer termMonths;

  @NotNull(message = "Tax rate is required")
  @DecimalMin(value = "0.0", message = "Tax rate cannot be negative")
  @DecimalMax(value = "100.0", message = "Tax rate cannot exceed 100%")
  private BigDecimal taxRate;
  
}
