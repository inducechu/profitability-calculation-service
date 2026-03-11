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
@Table(name = "deposits")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Deposit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  // In
  @Column(precision = 19, scale = 2)
  private BigDecimal amount;

  @Column(precision = 19, scale = 2)
  private BigDecimal interestRate;

  private Integer termMonths;
  private Boolean capitalization;

  // Out
  @Column(precision = 19, scale = 2)
  private BigDecimal finalAmount;

  @Column(precision = 19, scale = 2)
  private BigDecimal accruedInterest;

  @Column(precision = 5, scale = 2)
  private BigDecimal effectiveRate;

  // Metadata
  @Enumerated(EnumType.STRING)
  @Column(name = "compounding_frequency", nullable = false)
  private CompoundingFrequency frequency;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private LocalDateTime createdAt;
}
