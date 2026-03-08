package com.induce.profitability_calculation_service.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.induce.profitability_calculation_service.dto.DepositRequest;
import com.induce.profitability_calculation_service.dto.DepositResponse;
import com.induce.profitability_calculation_service.model.Deposit;
import com.induce.profitability_calculation_service.model.User;
import com.induce.profitability_calculation_service.repository.DepositRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepositService {

  private final DepositRepository depositRepository;

  public DepositResponse calculateAndSave(DepositRequest request, User user) {
    BigDecimal amount = request.getAmount();
    // Переводим годовую ставку в десятичную дробь (например, 15% -> 0.15)
    BigDecimal annualRate = request.getInterestRate().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
    int months = request.getTermMonths();

    BigDecimal finalAmount;

    if (request.isCapitalization()) {
      // Формула сложных процентов (ежемесячная капитализация): A = P * (1 + r/12)^m
      BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
      finalAmount = amount.multiply(monthlyRate.add(BigDecimal.ONE).pow(months))
          .setScale(2, RoundingMode.HALF_UP);
    } else {
      // Простые проценты: A = P * (1 + r * (m/12))
      BigDecimal termInYears = BigDecimal.valueOf(months).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
      finalAmount = amount.add(amount.multiply(annualRate).multiply(termInYears))
          .setScale(2, RoundingMode.HALF_UP);
    }

    BigDecimal accruedInterest = finalAmount.subtract(amount);

    // Считаем эффективную ставку: ((A/P) - 1) / (m/12) * 100
    BigDecimal termInYears = BigDecimal.valueOf(months).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
    BigDecimal effectiveRate = finalAmount.divide(amount, 10, RoundingMode.HALF_UP)
        .subtract(BigDecimal.ONE)
        .divide(termInYears, 10, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(100))
        .setScale(2, RoundingMode.HALF_UP);

    // Сохраняем результат в БД
    Deposit deposit = Deposit.builder()
        .amount(amount)
        .interestRate(request.getInterestRate())
        .termMonths(months)
        .capitalization(request.isCapitalization())
        .interval(request.getInterval())
        .finalAmount(finalAmount)
        .accruedInterest(accruedInterest)
        .effectiveRate(effectiveRate)
        .user(user)
        .createdAt(LocalDateTime.now())
        .build();

    depositRepository.save(deposit);

    return DepositResponse.builder()
        .finalAmount(finalAmount)
        .accruedInterest(accruedInterest)
        .effectiveRate(effectiveRate)
        .build();
  }
}
