package br.com.nubank.application;

import br.com.nubank.application.operator.AccountOperationContext;
import br.com.nubank.application.operator.FinanceOperationContext;
import br.com.nubank.application.operator.FinanceOperationProcessor;
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
        AccountOperationContext accountOperationContext = new AccountOperationContext();
        return financeOperations.stream()
                .map(financeOperation -> {
                    FinanceOperationProcessor processor = financeOperationProcessorFactory.getProcessor(financeOperation.getOperation());
                    return processor.preValidate(financeOperation, accountOperationContext).orElseGet(() -> processor.process(financeOperation, accountOperationContext.getFinanceOperationContext(financeOperation.getTicker())));
                }).collect(Collectors.toList());
    }


}
