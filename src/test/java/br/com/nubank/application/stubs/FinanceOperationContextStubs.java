package br.com.nubank.application.stubs;

import br.com.nubank.application.operator.FinanceOperationContext;

import java.math.BigDecimal;

public class FinanceOperationContextStubs {

    public static FinanceOperationContext withAverageCostAndTotalShares(BigDecimal averageCost, long totalShares) {
        FinanceOperationContext financeOperationContext = new FinanceOperationContext();
        financeOperationContext.setAverageCost(averageCost);
        financeOperationContext.setTotalShares(totalShares);
        return financeOperationContext;
    }

    public static FinanceOperationContext withAverageCostAndLoss(BigDecimal averageCost, BigDecimal loss) {
        FinanceOperationContext financeOperationContext = new FinanceOperationContext();
        financeOperationContext.setAverageCost(averageCost);
        financeOperationContext.setLoss(loss);
        return financeOperationContext;
    }

    public static FinanceOperationContext withAverageCostLossAndShares(BigDecimal averageCost, BigDecimal loss, Long shares) {
        FinanceOperationContext financeOperationContext = new FinanceOperationContext();
        financeOperationContext.setAverageCost(averageCost);
        financeOperationContext.setLoss(loss);
        financeOperationContext.setTotalShares(shares);
        return financeOperationContext;
    }
}
