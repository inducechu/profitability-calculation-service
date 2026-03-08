package com.induce.profitability_calculation_service.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BondResponse {

    private BigDecimal ytm;

    private BigDecimal netYield;

    private BigDecimal totalProfitAmount;
    
    private BigDecimal totalProfitPercent;
    
}

