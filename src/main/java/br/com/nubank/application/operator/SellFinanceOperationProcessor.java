package br.com.nubank.application.operator;

import br.com.nubank.domain.model.FinanceOperation;
import br.com.nubank.domain.model.FinanceOperationResult;
import br.com.nubank.domain.model.FinanceOperationResultError;
import br.com.nubank.domain.model.TaxFinanceOperationResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class SellFinanceOperationProcessor implements FinanceOperationProcessor {

    public static final int MINIMUM_PROFIT_TAXABLE_AMOUNT = 20000;

    @Override
    public FinanceOperationResult process(FinanceOperation financeOperation, FinanceOperationContext financeOperationContext) {
        BigDecimal profit = calculateProfit(financeOperation.getUnitCost(), financeOperation.getQuantity(), financeOperationContext.getAverageCost());
        BigDecimal tax = calculateTax(financeOperationContext.getLoss(), financeOperation.getUnitCost(), financeOperation.getQuantity(), profit);
        BigDecimal loss = calculatePreviousLoss(financeOperationContext.getLoss(), profit);
        financeOperationContext.setLoss(loss);
        financeOperationContext.subtractShares(financeOperation.getQuantity());
        return new TaxFinanceOperationResult(tax.setScale(2, RoundingMode.HALF_EVEN));
    }

    @Override
    public FinanceOperation.Operation getOperationType() {
        return FinanceOperation.Operation.SELL;
    }

    @Override
    public Optional<FinanceOperationResult> validate(FinanceOperation financeOperation, FinanceOperationContext financeOperationContext) {
        if(financeOperation.getQuantity() > financeOperationContext.getTotalShares()){
            return Optional.of(new FinanceOperationResultError("Can't sell more stocks than you have"));
        }
        return Optional.empty();
    }

    public BigDecimal calculateProfit(BigDecimal unitCost, long quantity, BigDecimal avgCost) {
        BigDecimal totalSaleValue = unitCost.multiply(BigDecimal.valueOf(quantity));
        BigDecimal totalInvestment = avgCost.multiply(BigDecimal.valueOf(quantity));
        return totalSaleValue.subtract(totalInvestment);
    }

    private BigDecimal calculatePreviousLoss(BigDecimal previousLoss, BigDecimal profit) {

        if (profit.compareTo(BigDecimal.ZERO) < 0) {
            return previousLoss.add(profit.abs());
        } else if (previousLoss.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal deductible = previousLoss.min(profit);
            return previousLoss.subtract(deductible);
        }

        return previousLoss;
    }

    private BigDecimal calculateTax(BigDecimal previousLoss, BigDecimal unitCost, long quantity, BigDecimal profit) {
        BigDecimal totalSaleValue = unitCost.multiply(BigDecimal.valueOf(quantity));

        if (profit.compareTo(BigDecimal.ZERO) >= 0) {
            if (previousLoss.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal deductible = previousLoss.min(profit);
                profit = profit.subtract(deductible);
            }

            if (totalSaleValue.compareTo(BigDecimal.valueOf(MINIMUM_PROFIT_TAXABLE_AMOUNT)) > 0) {
                return profit.multiply(BigDecimal.valueOf(0.2));
            }
        }

        return BigDecimal.ZERO;
    }
}
