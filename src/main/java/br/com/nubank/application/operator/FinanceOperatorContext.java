package br.com.nubank.application.operator;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class FinanceOperatorContext {
    private BigDecimal averageCost = BigDecimal.ZERO;
    private long totalShares = 0L;
    private BigDecimal loss = BigDecimal.ZERO;

    public FinanceOperatorContext(BigDecimal tax, BigDecimal zero, int i) {
    }

    public void addShares(long quantity) {
        this.totalShares += quantity;
    }

    public void subtractShares(long quantity) {
        this.totalShares -= quantity;
    }
}
