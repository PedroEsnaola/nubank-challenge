package br.com.nubank.application;

import br.com.nubank.application.operator.BuyFinanceOperationProcessor;
import br.com.nubank.application.operator.FinanceOperationProcessorFactory;
import br.com.nubank.application.operator.SellFinanceOperationProcessor;
import br.com.nubank.domain.model.FinanceOperation;
import br.com.nubank.domain.model.FinanceOperation.Operation;
import br.com.nubank.domain.model.FinanceOperationResult;
import br.com.nubank.domain.model.FinanceOperationResultError;
import br.com.nubank.domain.model.TaxFinanceOperationResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static br.com.nubank.application.stubs.FinanceOperationStubs.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class FinanceOperationsCalculatorTest {

    FinanceOperationProcessorFactory financeOperationProcessorFactory = new FinanceOperationProcessorFactory(Arrays.asList(
            new BuyFinanceOperationProcessor(),
            new SellFinanceOperationProcessor()
    ));
    FinanceOperationsCalculator underTest = new FinanceOperationsCalculator(financeOperationProcessorFactory);

    @ParameterizedTest
    @MethodSource("getStubs")
    @DisplayName("givenListOfOperations_whenProcessing_shouldReturnExpectedResults")
    void givenListOfOperations_whenProcessing_shouldReturnExpectedResults(List<FinanceOperation> operations,  List <FinanceOperationResult> expectedResults) {
        List<FinanceOperationResult> actualResults = underTest.processFinanceOperations(operations);
        assertThat(actualResults).hasSize(expectedResults.size());
        for (int i = 0; i < expectedResults.size(); i++) {
            assertThat(actualResults.get(i)).isEqualTo(expectedResults.get(i));
        }
    }


    static Stream<Arguments> getStubs() {
        return Stream.of(
                Arguments.of(Arrays.asList(
                                buying(BigDecimal.valueOf(10.00), 10000),
                                selling(BigDecimal.valueOf(2.00), 5000),
                                selling(BigDecimal.valueOf(20.00), 2000),
                                selling(BigDecimal.valueOf(20.00), 2000),
                                selling(BigDecimal.valueOf(25.00), 1000),
                                buying(BigDecimal.valueOf(20.00), 10000),
                                selling(BigDecimal.valueOf(15.00), 5000),
                                selling(BigDecimal.valueOf(30.00), 4350),
                                selling(BigDecimal.valueOf(30.00), 650)
                        ),
                        Arrays.asList(
                                new TaxFinanceOperationResult(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_EVEN)),
                                new TaxFinanceOperationResult(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_EVEN)),
                                new TaxFinanceOperationResult(BigDecimal.valueOf(0.000).setScale(2, RoundingMode.HALF_EVEN)),
                                new TaxFinanceOperationResult(BigDecimal.valueOf(0.000).setScale(2, RoundingMode.HALF_EVEN)),
                                new TaxFinanceOperationResult(BigDecimal.valueOf(3000.000).setScale(2, RoundingMode.HALF_EVEN)),
                                new TaxFinanceOperationResult(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_EVEN)),
                                new TaxFinanceOperationResult(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_EVEN)),
                                new TaxFinanceOperationResult(BigDecimal.valueOf(3700.000).setScale(2, RoundingMode.HALF_EVEN)),
                                new TaxFinanceOperationResult(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_EVEN))
                        )),
                Arguments.of(
                        Arrays.asList(
                                buying(BigDecimal.valueOf(10.00), 10000),
                                selling(BigDecimal.valueOf(50.00), 10000),
                                buying(BigDecimal.valueOf(20.00), 10000),
                                selling(BigDecimal.valueOf(50.00), 10000)
                        ),
                        Arrays.asList(
                                new TaxFinanceOperationResult(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_EVEN)),
                                new TaxFinanceOperationResult(BigDecimal.valueOf(80000.000).setScale(2, RoundingMode.HALF_EVEN)),
                                new TaxFinanceOperationResult(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_EVEN)),
                                new TaxFinanceOperationResult(BigDecimal.valueOf(60000.000).setScale(2, RoundingMode.HALF_EVEN))
                        )
                ),
                Arguments.of(
                        Arrays.asList(
                                buying(BigDecimal.valueOf(10.00), 10000),
                                selling(BigDecimal.valueOf(2.00), 5000),
                                selling(BigDecimal.valueOf(20.00), 2000),
                                selling(BigDecimal.valueOf(20.00), 2000),
                                selling(BigDecimal.valueOf(25.00), 1000)
                        ),
                        Arrays.asList(
                                new TaxFinanceOperationResult(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_EVEN)),
                                new TaxFinanceOperationResult(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_EVEN)),
                                new TaxFinanceOperationResult(BigDecimal.valueOf(0.000).setScale(2, RoundingMode.HALF_EVEN)),
                                new TaxFinanceOperationResult(BigDecimal.valueOf(0.000).setScale(2, RoundingMode.HALF_EVEN)),
                                new TaxFinanceOperationResult(BigDecimal.valueOf(3000.000).setScale(2, RoundingMode.HALF_EVEN))
                        )
                ),
                Arguments.of(
                        Arrays.asList(
                                selling(BigDecimal.valueOf(20.00), 10000),
                                selling(BigDecimal.valueOf(20.00), 10000),
                                buying(BigDecimal.valueOf(10.00), 10000),
                                selling(BigDecimal.valueOf(10.00), 10000),
                                selling(BigDecimal.valueOf(20.00), 10000),
                                selling(BigDecimal.valueOf(20.00), 10000),
                                selling(BigDecimal.valueOf(20.00), 10000),
                                selling(BigDecimal.valueOf(20.00), 10000)
                        ),
                        Arrays.asList(
                                new FinanceOperationResultError("Can't sell more stocks than you have"),
                                new FinanceOperationResultError("Can't sell more stocks than you have"),
                                new TaxFinanceOperationResult(BigDecimal.valueOf(0.000).setScale(2, RoundingMode.HALF_EVEN)),
                                new TaxFinanceOperationResult(BigDecimal.valueOf(0.000).setScale(2, RoundingMode.HALF_EVEN)),
                                new FinanceOperationResultError("Can't sell more stocks than you have"),
                                new FinanceOperationResultError("Can't sell more stocks than you have"),
                                new FinanceOperationResultError("Can't sell more stocks than you have"),
                                new FinanceOperationResultError("Your account is blocked")
                        )
                )
        );
    }



}