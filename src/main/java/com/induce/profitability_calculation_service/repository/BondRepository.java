package com.induce.profitability_calculation_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.induce.profitability_calculation_service.model.Bond;

public interface BondRepository extends JpaRepository<Bond, Long> {
  List<Bond> findAllByUserId(Long userId);
}
