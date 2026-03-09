package com.induce.profitability_calculation_service.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.induce.profitability_calculation_service.dto.StockRequest;
import com.induce.profitability_calculation_service.dto.StockResponse;
import com.induce.profitability_calculation_service.model.InvestmentType;
import com.induce.profitability_calculation_service.model.Stock;
import com.induce.profitability_calculation_service.model.User;
import com.induce.profitability_calculation_service.repository.StockRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockService implements CalculationStrategy<StockRequest, StockResponse> {

  private final StockRepository stockRepository;

  @Override
  public InvestmentType getType() {
    return InvestmentType.STOCK;
  }

  @Override
  @Transactional
  public StockResponse calculateAndSave(StockRequest request, User user) {
    BigDecimal purchasePrice = request.getPurchasePrice();
    BigDecimal currentPrice = request.getCurrentPrice();
    BigDecimal holdingMonths = BigDecimal.valueOf(request.getHoldingMonths());

    // 1. Доход от роста курса (Capital Gain)
    BigDecimal priceDifference = currentPrice.subtract(purchasePrice);

    // 2. Комиссия (считаем от суммы покупки)
    BigDecimal commissionMultiplier = request.getCommission().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
    BigDecimal commissionAmount = purchasePrice.multiply(commissionMultiplier);

    BigDecimal netCapitalGain = priceDifference.subtract(commissionAmount);

    // 3. Дивидендный доход
    // Считаем общую ставку за весь период владения (упрощенно: годовая ставка *
    // (месяцы/12))
    BigDecimal annualDivRate = request.getDividendRate().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
    BigDecimal yearsHolding = holdingMonths.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
    BigDecimal totalDividendIncome = purchasePrice.multiply(annualDivRate).multiply(yearsHolding);

    // 4. Общая доходность в рублях
    BigDecimal totalYieldAmount = netCapitalGain.add(totalDividendIncome);

    // 5. Общая доходность в % (годовых для сравнения)
    BigDecimal totalYieldPercent = totalYieldAmount
        .divide(purchasePrice, 10, RoundingMode.HALF_UP)
        .divide(yearsHolding, 10, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(100))
        .setScale(2, RoundingMode.HALF_UP);

    // Сохраняем в БД
    Stock stock = Stock.builder()
        .purchasePrice(purchasePrice)
        .currentPrice(currentPrice)
        .holdingMonths(request.getHoldingMonths())
        .dividendRate(request.getDividendRate())
        .dividendFrequency(request.getDividendFrequency())
        .commission(request.getCommission())
        .totalYieldPercent(totalYieldPercent)
        .totalYieldAmount(totalYieldAmount.setScale(2, RoundingMode.HALF_UP))
        .dividendIncome(totalDividendIncome.setScale(2, RoundingMode.HALF_UP))
        .capitalGain(netCapitalGain.setScale(2, RoundingMode.HALF_UP))
        .user(user)
        .createdAt(LocalDateTime.now())
        .build();

    stockRepository.save(stock);

    return StockResponse.builder()
        .totalYieldPercent(totalYieldPercent)
        .totalYieldAmount(totalYieldAmount.setScale(2, RoundingMode.HALF_UP))
        .capitalGain(netCapitalGain.setScale(2, RoundingMode.HALF_UP))
        .dividendIncome(totalDividendIncome.setScale(2, RoundingMode.HALF_UP))
        .build();
  }
}
