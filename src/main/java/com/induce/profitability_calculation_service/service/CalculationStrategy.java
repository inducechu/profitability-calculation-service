package com.induce.profitability_calculation_service.service;

import com.induce.profitability_calculation_service.model.InvestmentType;
import com.induce.profitability_calculation_service.model.User;

public interface CalculationStrategy<T, R> {

  InvestmentType getType();

  R calculateAndSave(T request, User user);
}
