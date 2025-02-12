package br.com.nubank.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinanceOperation {

    private Operation operation;
    @JsonProperty("unit-cost")
    private BigDecimal unitCost;
    private long quantity;


    public enum Operation {
        BUY, SELL;
    }

}
