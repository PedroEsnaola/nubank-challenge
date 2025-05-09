package br.com.nubank.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinanceOperationResultError implements FinanceOperationResult {
    private String error;
}
