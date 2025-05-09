package br.com.nubank.application.operator;

import br.com.nubank.domain.model.FinanceOperation;
import br.com.nubank.domain.model.FinanceOperationResult;
import br.com.nubank.domain.model.FinanceOperationResultError;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface FinanceOperationProcessor {

    FinanceOperationResult process(FinanceOperation financeOperation, FinanceOperationContext financeOperationContext);

    FinanceOperation.Operation getOperationType();

    Optional<FinanceOperationResult> validate(FinanceOperation financeOperation, FinanceOperationContext financeOperationContext);

    default Optional<FinanceOperationResult> preValidate(FinanceOperation financeOperation, AccountOperationContext financeOperationContext) {
        if (financeOperationContext.getErrorCount() >= 3) {
           return Optional.of(new FinanceOperationResultError("Your account is blocked"));
        }
        Optional<FinanceOperationResult> validate = validate(financeOperation, financeOperationContext.getFinanceOperationContext(financeOperation.getTicker()));
        validate.ifPresentOrElse(err -> financeOperationContext.incrementErrorCount(), financeOperationContext::resetErrorCount);
        return validate;
    }
}
