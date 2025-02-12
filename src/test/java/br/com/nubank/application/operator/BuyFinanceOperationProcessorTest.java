package br.com.nubank.application.operator;

import br.com.nubank.application.stubs.FinanceOperationContextStubs;
import br.com.nubank.domain.model.FinanceOperationResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static br.com.nubank.application.stubs.FinanceOperationStubs.buying;
import static org.assertj.core.api.Assertions.assertThat;

class BuyFinanceOperationProcessorTest {

    BuyFinanceOperationProcessor underTest = new BuyFinanceOperationProcessor();

    @Test
    @DisplayName("Given a valid finance operation and context, when processing, should update average cost and total shares correctly")
    void givenValidOperationAndContext_whenProcessing_shouldUpdateAverageCostAndTotalSharesCorrectly() {
        var context = FinanceOperationContextStubs.withAverageCostAndTotalShares(BigDecimal.valueOf(20), 100);
        var result = underTest.process(buying(new BigDecimal("30.00"), 50), context);

        BigDecimal expectedNewAverageCost = new BigDecimal("23.33");
        assertThat(context.getAverageCost()).isEqualByComparingTo(expectedNewAverageCost);
        assertThat(context.getTotalShares()).isEqualTo(150L);
        assertThat(result).isNotNull();
        assertThat(result.getTax()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Given zero initial shares, when processing, should calculate average cost correctly")
    void givenZeroInitialShares_whenProcessing_shouldCalculateAverageCostCorrectly() {
        var context = FinanceOperationContextStubs.withAverageCostAndTotalShares(BigDecimal.valueOf(0), 0);
        var result = underTest.process(buying(new BigDecimal("40.00"), 100), context);

        BigDecimal expectedNewAverageCost = new BigDecimal("40.00");
        assertThat(context.getAverageCost()).isEqualByComparingTo(expectedNewAverageCost);
        assertThat(context.getTotalShares()).isEqualTo(100L);
        assertThat(result).isNotNull();
        assertThat(result.getTax()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Given a high precision unit cost, when processing, should normalize average cost to two decimal places")
    void givenHighPrecisionUnitCost_whenProcessing_shouldNormalizeAverageCostToTwoDecimalPlaces() {
        var context = FinanceOperationContextStubs.withAverageCostAndTotalShares(BigDecimal.valueOf(15), 200);
        var result = underTest.process(buying(new BigDecimal("25.123456"), 300), context);

        BigDecimal expectedNewAverageCost = new BigDecimal("21.07");
        assertThat(context.getAverageCost()).isEqualByComparingTo(expectedNewAverageCost);
        assertThat(context.getTotalShares()).isEqualTo(500L);
        assertThat(result).isNotNull();
        assertThat(result.getTax()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}