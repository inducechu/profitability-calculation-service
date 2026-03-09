package com.induce.profitability_calculation_service.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.induce.profitability_calculation_service.model.InvestmentType;

@Component
public class CalculationFactory {

  private final Map<InvestmentType, CalculationStrategy<?, ?>> strategies;

  public CalculationFactory(List<CalculationStrategy<?, ?>> strategyList) {
    this.strategies = strategyList.stream()
        .collect(Collectors.toMap(CalculationStrategy::getType, s -> s));
  }

  public CalculationStrategy<?, ?> getStrategy(InvestmentType type) {
    CalculationStrategy<?, ?> strategy = strategies.get(type);
    if (strategy == null) {
      throw new IllegalArgumentException("No strategy found for type: " + type);
    }
    return strategy;
  }
}
