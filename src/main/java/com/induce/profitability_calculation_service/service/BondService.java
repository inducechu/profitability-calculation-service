package com.induce.profitability_calculation_service.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.induce.profitability_calculation_service.dto.BondRequest;
import com.induce.profitability_calculation_service.dto.BondResponse;
import com.induce.profitability_calculation_service.model.Bond;
import com.induce.profitability_calculation_service.model.InvestmentType;
import com.induce.profitability_calculation_service.model.User;
import com.induce.profitability_calculation_service.repository.BondRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BondService implements CalculationStrategy<BondRequest, BondResponse> {

  private final BondRepository bondRepository;

  @Override
  public InvestmentType getType() {
    return InvestmentType.BOND;
  }

  @Override
  @Transactional
  public BondResponse calculateAndSave(BondRequest request, User user) {
    BigDecimal nominal = request.getNominal();
    int totalMonths = request.getTermMonths();
    int paymentStep = request.getFrequency().getMonths();

    // 1. Цена покупки в деньгах
    BigDecimal purchasePrice = nominal.multiply(request.getPurchasePricePercent())
        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

    // 2. Месячная ставка купона
    BigDecimal monthlyCouponRate = request.getCouponRate()
        .divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);

    List<BigDecimal> couponPaymentsGraph = new ArrayList<>();
    BigDecimal accumulatedCoupons = BigDecimal.ZERO;

    // 3. Формируем график накопленных купонов по интервалам
    for (int month = 1; month <= totalMonths; month++) {
      BigDecimal interestForMonth = nominal.multiply(monthlyCouponRate);
      accumulatedCoupons = accumulatedCoupons.add(interestForMonth);

      if (month % paymentStep == 0 || month == totalMonths) {
        couponPaymentsGraph.add(accumulatedCoupons.setScale(2, RoundingMode.HALF_UP));
      }
    }

    // 4. Прибыль от разницы цен
    BigDecimal priceGain = nominal.subtract(purchasePrice);
    BigDecimal grossProfit = accumulatedCoupons.add(priceGain);

    // 5. Налоги и чистая прибыль
    BigDecimal taxMultiplier = request.getTaxRate().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
    BigDecimal netProfit = grossProfit.compareTo(BigDecimal.ZERO) > 0
        ? grossProfit.subtract(grossProfit.multiply(taxMultiplier))
        : grossProfit;

    // 6. Расчет доходностей (годовых)
    BigDecimal termInYears = BigDecimal.valueOf(totalMonths).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

    // Грязная доходность (YTM)
    BigDecimal ytm = grossProfit.divide(purchasePrice, 10, RoundingMode.HALF_UP)
        .divide(termInYears, 10, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(100))
        .setScale(2, RoundingMode.HALF_UP);

    // Чистая доходность
    BigDecimal netYield = netProfit.divide(purchasePrice, 10, RoundingMode.HALF_UP)
        .divide(termInYears, 10, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(100))
        .setScale(2, RoundingMode.HALF_UP);

    // Общий профит в процентах за весь срок
    BigDecimal totalProfitPercent = netProfit.divide(purchasePrice, 10, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(100))
        .setScale(2, RoundingMode.HALF_UP);

    // 7. Сохранение в БД
    Bond bond = Bond.builder()
        .nominal(nominal)
        .purchasePricePercent(request.getPurchasePricePercent())
        .couponRate(request.getCouponRate())
        .frequency(request.getFrequency())
        .termMonths(totalMonths)
        .taxRate(request.getTaxRate())
        .ytm(ytm)
        .netYield(netYield)
        .totalProfitAmount(netProfit.setScale(2, RoundingMode.HALF_UP))
        .totalProfitPercent(totalProfitPercent)
        .user(user)
        .createdAt(LocalDateTime.now())
        .build();

    bondRepository.save(bond);

    return BondResponse.builder()
        .ytm(ytm)
        .netYield(netYield)
        .totalProfitAmount(netProfit.setScale(2, RoundingMode.HALF_UP))
        .totalProfitPercent(totalProfitPercent)
        .frequency(request.getFrequency())
        .couponPaymentsGraph(couponPaymentsGraph)
        .build();
  }
}
