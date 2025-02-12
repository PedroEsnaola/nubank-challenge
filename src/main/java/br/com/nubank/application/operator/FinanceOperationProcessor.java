package br.com.nubank.application.operator;

import br.com.nubank.domain.model.FinanceOperation;
import br.com.nubank.domain.model.FinanceOperationResult;

public interface FinanceOperationProcessor {

    FinanceOperationResult process(FinanceOperation financeOperation, FinanceOperatorContext financeOperatorContext);
    FinanceOperation.Operation getOperationType();

}
