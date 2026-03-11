package com.induce.profitability_calculation_service.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "bonds")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bond {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // In
  @Column(precision = 19, scale = 2)
  private BigDecimal nominal;

  @Column(precision = 5, scale = 2)
  private BigDecimal purchasePricePercent;

  @Column(precision = 5, scale = 2)
  private BigDecimal couponRate;

  private Integer termMonths;

  @Column(precision = 5, scale = 2)
  private BigDecimal taxRate;

  @Enumerated(EnumType.STRING)
  private FinancialFrequency frequency;

  // Out
  @Column(precision = 5, scale = 2)
  private BigDecimal ytm;

  @Column(precision = 5, scale = 2)
  private BigDecimal netYield;

  @Column(precision = 19, scale = 2)
  private BigDecimal totalProfitAmount;

  @Column(precision = 5, scale = 2)
  private BigDecimal totalProfitPercent;

  // Metadata
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private LocalDateTime createdAt;
}
