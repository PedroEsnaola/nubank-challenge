package br.com.nubank.application.operator;

import br.com.nubank.domain.model.FinanceOperation;
import br.com.nubank.domain.model.FinanceOperationResult;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BuyFinanceOperationProcessor implements FinanceOperationProcessor {
    @Override
    public FinanceOperationResult process(FinanceOperation financeOperation, FinanceOperationContext financeOperationContext) {
        financeOperationContext.setAverageCost(calculateNewAverageCost(financeOperationContext.getAverageCost(), financeOperationContext.getTotalShares(), financeOperation.getUnitCost(), financeOperation.getQuantity()));
        financeOperationContext.addShares(financeOperation.getQuantity());
        return new FinanceOperationResult(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN));
    }

    @Override
    public FinanceOperation.Operation getOperationType() {
        return FinanceOperation.Operation.BUY;
    }


    private BigDecimal calculateNewAverageCost(BigDecimal currentAvg, long currentShares, BigDecimal unitCost, long quantity) {
        return (currentAvg.multiply(BigDecimal.valueOf(currentShares))
                .add(unitCost.multiply(BigDecimal.valueOf(quantity))))
                .divide(BigDecimal.valueOf(currentShares + quantity), 2, RoundingMode.HALF_EVEN);
    }
}
