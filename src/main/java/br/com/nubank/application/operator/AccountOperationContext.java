package br.com.nubank.application.operator;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class AccountOperationContext {

    private final Map<String, FinanceOperationContext> contextMap = new HashMap<>();

    private int errorCount = 0;

    public void incrementErrorCount() {
        this.errorCount++;
    }

    public void resetErrorCount() {
        this.errorCount = 0;
    }

    public FinanceOperationContext getFinanceOperationContext(String ticker) {
        return this.contextMap.compute(ticker, (k, v) -> v == null ? new FinanceOperationContext() : v);
    }
}
