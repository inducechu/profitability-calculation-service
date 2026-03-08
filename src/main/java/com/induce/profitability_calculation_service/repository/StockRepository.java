package com.induce.profitability_calculation_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.induce.profitability_calculation_service.model.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {
  public List<Stock> findAllByUserId(Long userId);
}
