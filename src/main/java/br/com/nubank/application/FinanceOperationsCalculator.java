package br.com.nubank.application;

import br.com.nubank.application.operator.FinanceOperationContext;
import br.com.nubank.application.operator.FinanceOperationProcessorFactory;
import br.com.nubank.domain.model.FinanceOperation;
import br.com.nubank.domain.model.FinanceOperationResult;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FinanceOperationsCalculator {

    private final FinanceOperationProcessorFactory financeOperationProcessorFactory;

    public List<FinanceOperationResult> processFinanceOperations(List<FinanceOperation> financeOperations) {
        FinanceOperationContext financeOperationContext = new FinanceOperationContext();
        return financeOperations.stream()
                .map(financeOperation -> {
                    return financeOperationProcessorFactory.getProcessor(financeOperation.getOperation()).process(financeOperation, financeOperationContext);
                }).collect(Collectors.toList());
    }

}
