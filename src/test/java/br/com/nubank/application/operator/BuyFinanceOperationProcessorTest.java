package br.com.nubank.application.operator;

import br.com.nubank.domain.model.FinanceOperation;
import br.com.nubank.domain.model.FinanceOperationResult;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class BuyFinanceOperationProcessorTest {

    @Test
    @org.junit.jupiter.api.DisplayName("Given a valid finance operation and context, when processing, should update average cost and total shares correctly")
    void givenValidOperationAndContext_whenProcessing_shouldUpdateAverageCostAndTotalSharesCorrectly() {

        BuyFinanceOperationProcessor processor = new BuyFinanceOperationProcessor();

        FinanceOperatorContext financeOperatorContext = new FinanceOperatorContext();
        financeOperatorContext.setAverageCost(new BigDecimal("20.00"));
        financeOperatorContext.setTotalShares(100L);

        FinanceOperation financeOperation = new FinanceOperation();
        financeOperation.setUnitCost(new BigDecimal("30.00"));
        financeOperation.setQuantity(50L);


        FinanceOperationResult result = processor.process(financeOperation, financeOperatorContext);


        BigDecimal expectedNewAverageCost = new BigDecimal("23.33");
        assertThat(financeOperatorContext.getAverageCost()).isEqualByComparingTo(expectedNewAverageCost);
        assertThat(financeOperatorContext.getTotalShares()).isEqualTo(150L);
        assertThat(result).isNotNull();
        assertThat(result.getTax()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @org.junit.jupiter.api.DisplayName("Given zero initial shares, when processing, should calculate average cost correctly")
    void givenZeroInitialShares_whenProcessing_shouldCalculateAverageCostCorrectly() {

        BuyFinanceOperationProcessor processor = new BuyFinanceOperationProcessor();

        FinanceOperatorContext financeOperatorContext = new FinanceOperatorContext();
        financeOperatorContext.setAverageCost(BigDecimal.ZERO);
        financeOperatorContext.setTotalShares(0L);

        FinanceOperation financeOperation = new FinanceOperation();
        financeOperation.setUnitCost(new BigDecimal("40.00"));
        financeOperation.setQuantity(100L);


        FinanceOperationResult result = processor.process(financeOperation, financeOperatorContext);


        BigDecimal expectedNewAverageCost = new BigDecimal("40.00");
        assertThat(financeOperatorContext.getAverageCost()).isEqualByComparingTo(expectedNewAverageCost);
        assertThat(financeOperatorContext.getTotalShares()).isEqualTo(100L);
        assertThat(result).isNotNull();
        assertThat(result.getTax()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @org.junit.jupiter.api.DisplayName("Given a high precision unit cost, when processing, should normalize average cost to two decimal places")
    void givenHighPrecisionUnitCost_whenProcessing_shouldNormalizeAverageCostToTwoDecimalPlaces() {

        BuyFinanceOperationProcessor processor = new BuyFinanceOperationProcessor();

        FinanceOperatorContext financeOperatorContext = new FinanceOperatorContext();
        financeOperatorContext.setAverageCost(new BigDecimal("15.00"));
        financeOperatorContext.setTotalShares(200L);

        FinanceOperation financeOperation = new FinanceOperation();
        financeOperation.setUnitCost(new BigDecimal("25.123456"));
        financeOperation.setQuantity(300L);


        FinanceOperationResult result = processor.process(financeOperation, financeOperatorContext);


        BigDecimal expectedNewAverageCost = new BigDecimal("21.07");
        assertThat(financeOperatorContext.getAverageCost()).isEqualByComparingTo(expectedNewAverageCost);
        assertThat(financeOperatorContext.getTotalShares()).isEqualTo(500L);
        assertThat(result).isNotNull();
        assertThat(result.getTax()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}