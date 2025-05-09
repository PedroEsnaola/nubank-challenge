package br.com.nubank.domain.model;

import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxFinanceOperationResult implements FinanceOperationResult {
    private BigDecimal tax;
}
