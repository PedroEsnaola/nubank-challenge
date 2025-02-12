package br.com.nubank.application;

import br.com.nubank.application.operator.BuyFinanceOperationProcessor;
import br.com.nubank.application.operator.FinanceOperationProcessorFactory;
import br.com.nubank.application.operator.SellFinanceOperationProcessor;
import br.com.nubank.domain.model.FinanceOperation;
import br.com.nubank.domain.model.FinanceOperation.Operation;
import br.com.nubank.domain.model.FinanceOperationResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FinanceOperationsCalculatorTest {

    FinanceOperationProcessorFactory financeOperationProcessorFactory = new FinanceOperationProcessorFactory(Arrays.asList(
            new BuyFinanceOperationProcessor(),
            new SellFinanceOperationProcessor()
    ));
    FinanceOperationsCalculator underTest = new FinanceOperationsCalculator(financeOperationProcessorFactory);

    @Test
    @DisplayName("givenListOfOperations_whenProcessing_shouldReturnExpectedResults")
    void givenListOfOperations_whenProcessing_shouldReturnExpectedResults() {

        List<FinanceOperation> operations = Arrays.asList(
                new FinanceOperation(Operation.BUY, BigDecimal.valueOf(10.00), 10000),
                new FinanceOperation(Operation.SELL, BigDecimal.valueOf(2.00), 5000),
                new FinanceOperation(Operation.SELL, BigDecimal.valueOf(20.00), 2000),
                new FinanceOperation(Operation.SELL, BigDecimal.valueOf(20.00), 2000),
                new FinanceOperation(Operation.SELL, BigDecimal.valueOf(25.00), 1000),
                new FinanceOperation(Operation.BUY, BigDecimal.valueOf(20.00), 10000),
                new FinanceOperation(Operation.SELL, BigDecimal.valueOf(15.00), 5000),
                new FinanceOperation(Operation.SELL, BigDecimal.valueOf(30.00), 4350),
                new FinanceOperation(Operation.SELL, BigDecimal.valueOf(30.00), 650)
        );

        List<FinanceOperationResult> expectedResults = Arrays.asList(
                new FinanceOperationResult(BigDecimal.valueOf(0)),
                new FinanceOperationResult(BigDecimal.valueOf(0)),
                new FinanceOperationResult(BigDecimal.valueOf(0.000)),
                new FinanceOperationResult(BigDecimal.valueOf(0.000)),
                new FinanceOperationResult(BigDecimal.valueOf(3000.000)),
                new FinanceOperationResult(BigDecimal.valueOf(0)),
                new FinanceOperationResult(BigDecimal.valueOf(0)),
                new FinanceOperationResult(BigDecimal.valueOf(3700.000)),
                new FinanceOperationResult(BigDecimal.valueOf(0))
        );

        List<FinanceOperationResult> actualResults = underTest.processFinanceOperations(operations);

        assertThat(actualResults).hasSize(expectedResults.size());
        for (int i = 0; i < expectedResults.size(); i++) {
            assertThat(actualResults.get(i).getTax())
                    .as("Erro no índice: " + i)
                    .isEqualByComparingTo(expectedResults.get(i).getTax());
        }
    }

    @Test
    @DisplayName("givenAlternativeListOfOperations_whenProcessing_shouldReturnExpectedResults")
    void givenAlternativeListOfOperations_whenProcessing_shouldReturnExpectedResults() {


        List<FinanceOperation> operations = Arrays.asList(
                new FinanceOperation(Operation.BUY, BigDecimal.valueOf(10.00), 10000),
                new FinanceOperation(Operation.SELL, BigDecimal.valueOf(50.00), 10000),
                new FinanceOperation(Operation.BUY, BigDecimal.valueOf(20.00), 10000),
                new FinanceOperation(Operation.SELL, BigDecimal.valueOf(50.00), 10000)
        );

        List<FinanceOperationResult> expectedResults = Arrays.asList(
                new FinanceOperationResult(BigDecimal.valueOf(0)),
                new FinanceOperationResult(BigDecimal.valueOf(80000.000)),
                new FinanceOperationResult(BigDecimal.valueOf(0)),
                new FinanceOperationResult(BigDecimal.valueOf(60000.000))
        );

        List<FinanceOperationResult> actualResults = underTest.processFinanceOperations(operations);

        assertThat(actualResults).hasSize(expectedResults.size());
        for (int i = 0; i < expectedResults.size(); i++) {
            assertThat(actualResults.get(i).getTax())
                    .as("Erro no índice: " + i)
                    .isEqualByComparingTo(expectedResults.get(i).getTax());
        }
    }

    @Test
    @DisplayName("givenOperationsWithPartialSellAndLoss_whenProcessing_shouldReturnExpectedResults")
    void givenOperationsWithPartialSellAndLoss_whenProcessing_shouldReturnExpectedResults() {


        List<FinanceOperation> operations = Arrays.asList(
                new FinanceOperation(Operation.BUY, BigDecimal.valueOf(10.00), 10000),
                new FinanceOperation(Operation.SELL, BigDecimal.valueOf(2.00), 5000),
                new FinanceOperation(Operation.SELL, BigDecimal.valueOf(20.00), 2000),
                new FinanceOperation(Operation.SELL, BigDecimal.valueOf(20.00), 2000),
                new FinanceOperation(Operation.SELL, BigDecimal.valueOf(25.00), 1000)
        );

        List<FinanceOperationResult> expectedResults = Arrays.asList(
                new FinanceOperationResult(BigDecimal.valueOf(0)),
                new FinanceOperationResult(BigDecimal.valueOf(0)),
                new FinanceOperationResult(BigDecimal.valueOf(0.000)),
                new FinanceOperationResult(BigDecimal.valueOf(0.000)),
                new FinanceOperationResult(BigDecimal.valueOf(3000.000))
        );

        List<FinanceOperationResult> actualResults = underTest.processFinanceOperations(operations);

        assertThat(actualResults).hasSize(expectedResults.size());
        for (int i = 0; i < expectedResults.size(); i++) {
            assertThat(actualResults.get(i).getTax())
                    .as("Erro no índice: " + i)
                    .isEqualByComparingTo(expectedResults.get(i).getTax());
        }
    }
}