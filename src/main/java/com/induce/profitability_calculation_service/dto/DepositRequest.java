package com.induce.profitability_calculation_service.dto;

import java.math.BigDecimal;

import com.induce.profitability_calculation_service.model.CompoundingFrequency;

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
public class DepositRequest {
  @NotNull(message = "Deposit amount is required")
  @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
  private BigDecimal amount;

  @NotNull(message = "Interest rate is required")
  @DecimalMin(value = "0.0", message = "Interest rate cannot be negative")
  private BigDecimal interestRate;

  @NotNull(message = "Term in months is required")
  @Min(value = 1, message = "Term must be at least 1 month")
  private Integer termMonths;

  private boolean capitalization;

  @NotNull(message = "Compounding frequency is required")
    private CompoundingFrequency frequency;
}
