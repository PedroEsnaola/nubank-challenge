package br.com.nubank.application.operator;

import br.com.nubank.domain.model.FinanceOperation;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FinanceOperatorFactory {

    Map<FinanceOperation.Operation, FinanceOperationProcessor> processors;

    public FinanceOperatorFactory(List<FinanceOperationProcessor> processors) {
        this.processors = processors.stream().collect(Collectors.toMap(FinanceOperationProcessor::getOperationType, Function.identity()));
    }

    public FinanceOperationProcessor getProcessor(FinanceOperation.Operation operation) {
        return processors.get(operation);
    }
}
