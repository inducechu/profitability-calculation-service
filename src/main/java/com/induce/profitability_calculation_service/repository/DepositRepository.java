package com.induce.profitability_calculation_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.induce.profitability_calculation_service.model.Deposit;

public interface DepositRepository extends JpaRepository<Deposit, Long> {
  List<Deposit> findAllByUserId(Long userId);
}