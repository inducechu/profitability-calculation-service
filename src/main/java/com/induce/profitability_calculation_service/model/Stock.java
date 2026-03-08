package com.induce.profitability_calculation_service.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stocks")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stock {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // In
  @Column(precision = 19, scale = 2)
  private BigDecimal purchasePrice;

  @Column(precision = 19, scale = 2)
  private BigDecimal currentPrice;

  private Integer holdingMonths;

  @Column(precision = 5, scale = 2)
  private BigDecimal dividendRate;

  private String dividendFrequency;

  @Column(precision = 5, scale = 2)
  private BigDecimal commission;

  // Out
  @Column(precision = 5, scale = 2)
  private BigDecimal totalYieldPercent;

  @Column(precision = 19, scale = 2)
  private BigDecimal totalYieldAmount;

  @Column(precision = 19, scale = 2)
  private BigDecimal dividendIncome;

  @Column(precision = 19, scale = 2)
  private BigDecimal capitalGain;

  // Metadata
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private LocalDateTime createdAt;
}
