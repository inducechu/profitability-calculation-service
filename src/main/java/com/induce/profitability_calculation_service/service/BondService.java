package com.induce.profitability_calculation_service.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

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
    // Цена покупки в деньгах = Номинал * (Процент / 100)
    BigDecimal purchasePrice = nominal.multiply(request.getPurchasePricePercent())
        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

    BigDecimal years = BigDecimal.valueOf(request.getTermYears());

    // 1. Суммарный купонный доход за весь срок
    BigDecimal totalCoupons = nominal.multiply(request.getCouponRate())
        .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
        .multiply(years);

    // 2. Доход от разницы цен (номинал при погашении - цена покупки)
    BigDecimal priceGain = nominal.subtract(purchasePrice);

    // 3. Общая грязная прибыль
    BigDecimal grossProfit = totalCoupons.add(priceGain);

    // 4. Налоги (применяем taxRate к прибыли)
    BigDecimal taxMultiplier = request.getTaxRate().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
    BigDecimal taxAmount = grossProfit.compareTo(BigDecimal.ZERO) > 0
        ? grossProfit.multiply(taxMultiplier)
        : BigDecimal.ZERO;

    // 5. Чистая прибыль
    BigDecimal netProfit = grossProfit.subtract(taxAmount);

    // 6. Расчет YTM (упрощенная формула: (Купон + (Номинал-Цена)/Срок) /
    // ((Номинал+Цена)/2))
    BigDecimal annualCoupon = totalCoupons.divide(years, 10, RoundingMode.HALF_UP);
    BigDecimal priceGainPerYear = priceGain.divide(years, 10, RoundingMode.HALF_UP);
    BigDecimal averagePrice = nominal.add(purchasePrice).divide(BigDecimal.valueOf(2), 10, RoundingMode.HALF_UP);

    BigDecimal ytm = annualCoupon.add(priceGainPerYear)
        .divide(averagePrice, 10, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(100))
        .setScale(2, RoundingMode.HALF_UP);

    // 7. Чистая доходность (Net Yield)
    BigDecimal netYield = ytm.multiply(BigDecimal.ONE.subtract(taxMultiplier))
        .setScale(2, RoundingMode.HALF_UP);

    // 8. Общая прибыль в % от вложенного
    BigDecimal totalProfitPercent = netProfit.divide(purchasePrice, 10, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(100))
        .setScale(2, RoundingMode.HALF_UP);

    // Сохраняем в БД
    Bond bond = Bond.builder()
        .nominal(nominal)
        .purchasePricePercent(request.getPurchasePricePercent())
        .couponRate(request.getCouponRate())
        .couponFrequency(request.getCouponFrequency())
        .termYears(request.getTermYears())
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
        .build();
  }
}
