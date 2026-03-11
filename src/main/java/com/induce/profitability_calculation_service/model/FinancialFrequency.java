package com.induce.profitability_calculation_service.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FinancialFrequency {
    MONTHLY(1),
    QUARTERLY(3),
    SEMI_ANNUALLY(6),
    ANNUALLY(12);

    private final int months;
}
