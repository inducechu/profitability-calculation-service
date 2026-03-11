package com.induce.profitability_calculation_service.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.induce.profitability_calculation_service.dto.DepositRequest;
import com.induce.profitability_calculation_service.dto.DepositResponse;
import com.induce.profitability_calculation_service.model.Deposit;
import com.induce.profitability_calculation_service.model.InvestmentType;
import com.induce.profitability_calculation_service.model.User;
import com.induce.profitability_calculation_service.repository.DepositRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepositService implements CalculationStrategy<DepositRequest, DepositResponse> {

  private final DepositRepository depositRepository;

  @Override
  public InvestmentType getType() {
    return InvestmentType.DEPOSIT;
  }

  @Override
  @Transactional
  public DepositResponse calculateAndSave(DepositRequest request, User user) {
    BigDecimal initialAmount = request.getAmount();
    int totalMonths = request.getTermMonths();
    int compoundStep = request.getFrequency().getMonths();

    // Месячная простая ставка: (r / 100) / 12
    BigDecimal monthlyRate = request.getInterestRate()
        .divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);

    List<BigDecimal> graph = new ArrayList<>();
    BigDecimal currentPrincipal = initialAmount;
    BigDecimal accumulatedInterest = BigDecimal.ZERO;

    for (int month = 1; month <= totalMonths; month++) {
      // 1. Начисляем проценты за текущий месяц
      BigDecimal interestForThisMonth = currentPrincipal.multiply(monthlyRate);
      accumulatedInterest = accumulatedInterest.add(interestForThisMonth);

      // 2. Проверяем условие капитализации
      if (request.isCapitalization() && month % compoundStep == 0) {
        currentPrincipal = currentPrincipal.add(accumulatedInterest);
        accumulatedInterest = BigDecimal.ZERO;
      }

      // 3. Оптимизация графика: добавляем точку только в конце интервала (шага)
      // Или если это самый последний месяц вклада
      if (month % compoundStep == 0 || month == totalMonths) {
        BigDecimal currentTotal = currentPrincipal.add(accumulatedInterest);
        graph.add(currentTotal.setScale(2, RoundingMode.HALF_UP));
      }
    }

    BigDecimal finalAmount = currentPrincipal.add(accumulatedInterest).setScale(2, RoundingMode.HALF_UP);
    BigDecimal accruedInterest = finalAmount.subtract(initialAmount);

    BigDecimal termInYears = BigDecimal.valueOf(totalMonths).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
    BigDecimal effectiveRate = finalAmount.divide(initialAmount, 10, RoundingMode.HALF_UP)
        .subtract(BigDecimal.ONE)
        .divide(termInYears, 10, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(100))
        .setScale(2, RoundingMode.HALF_UP);

    // Сохраняем результат в БД
    Deposit deposit = Deposit.builder()
        .amount(initialAmount)
        .interestRate(request.getInterestRate())
        .termMonths(totalMonths)
        .capitalization(request.isCapitalization())
        .frequency(request.getFrequency())
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
        .frequency(request.getFrequency())
        .capitalGrowthGraph(graph)
        .build();
  }
}
