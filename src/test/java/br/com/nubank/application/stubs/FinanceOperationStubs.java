package br.com.nubank.application.stubs;

import br.com.nubank.domain.model.FinanceOperation;

import java.math.BigDecimal;

public class FinanceOperationStubs {

    public static FinanceOperation buying(BigDecimal unitCost, long quantity) {
        return new FinanceOperation(FinanceOperation.Operation.BUY, unitCost, quantity);
    }

    public static FinanceOperation selling(BigDecimal unitCost, long quantity) {
        return new FinanceOperation(FinanceOperation.Operation.SELL, unitCost, quantity);
    }
}
