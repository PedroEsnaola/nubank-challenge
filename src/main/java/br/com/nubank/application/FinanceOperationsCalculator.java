package br.com.nubank.application;

import br.com.nubank.application.operator.FinanceOperatorContext;
import br.com.nubank.application.operator.FinanceOperatorFactory;
import br.com.nubank.domain.model.FinanceOperation;
import br.com.nubank.domain.model.FinanceOperationResult;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FinanceOperationsCalculator {

    private final FinanceOperatorFactory financeOperatorFactory;

    public List<FinanceOperationResult> processFinanceOperations(List<FinanceOperation> financeOperations) {
        FinanceOperatorContext financeOperatorContext = new FinanceOperatorContext();
        return financeOperations.stream()
                .map(financeOperation -> {
                    return financeOperatorFactory.getProcessor(financeOperation.getOperation()).process(financeOperation, financeOperatorContext);
                }).collect(Collectors.toList());
    }

}
