package br.com.nubank.application.operator;

import br.com.nubank.application.stubs.FinanceOperationContextStubs;
import br.com.nubank.domain.model.FinanceOperation;
import br.com.nubank.domain.model.FinanceOperationResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static br.com.nubank.application.stubs.FinanceOperationStubs.selling;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class SellFinanceOperationProcessorTest {

    SellFinanceOperationProcessor underTest = new SellFinanceOperationProcessor();

    @Test
    @DisplayName("Given a high profit on shares sale, when selling, should calculate tax correctly")
    void givenHighProfit_whenSellingShares_shouldCalculateTaxCorrectly() {
        var context = FinanceOperationContextStubs.withAverageCostAndLoss(BigDecimal.valueOf(100), BigDecimal.ZERO);
        var result = underTest.process(selling(BigDecimal.valueOf(150), 200), context);

        assertThat(result.getTax()).isEqualByComparingTo(BigDecimal.valueOf(2000.00).setScale(2));
        assertThat(context.getLoss()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Given no profit, when selling shares, should calculate zero tax")
    void givenNoProfit_whenSellingShares_shouldCalculateZeroTax() {
        var context = FinanceOperationContextStubs.withAverageCostAndLoss(BigDecimal.valueOf(100), BigDecimal.ZERO);
        var result = underTest.process(selling(BigDecimal.valueOf(100), 50), context);

        assertThat(result.getTax()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(context.getLoss()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Given a previous loss, when selling at profit, should calculate tax after deductible loss and dont pay taxed due to low profit")
    void givenPreviousLoss_whenSellingAtProfit_shouldDeductLossBeforeCalculatingTaxAndDontPayTaxesDueToLowProfit() {
        var context = FinanceOperationContextStubs.withAverageCostAndLoss(BigDecimal.valueOf(100), BigDecimal.valueOf(2000));
        var result = underTest.process(selling(BigDecimal.valueOf(150), 100), context);

        assertThat(result.getTax()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(context.getLoss()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Given a loss, when selling shares, should update context with new loss")
    void givenALoss_whenSellingShares_shouldUpdateLossInContext() {
        var context = FinanceOperationContextStubs.withAverageCostAndLoss(BigDecimal.valueOf(100), BigDecimal.valueOf(500));
        var result = underTest.process(selling(BigDecimal.valueOf(70), 100), context);

        assertThat(result.getTax()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(context.getLoss()).isEqualByComparingTo(BigDecimal.valueOf(3500));
    }

    @Test
    @DisplayName("Given high sale value but no profit, when selling shares, should calculate zero tax")
    void givenHighSaleValueNoProfit_whenSellingShares_shouldCalculateZeroTax() {
        var context = FinanceOperationContextStubs.withAverageCostAndLoss(BigDecimal.valueOf(200), BigDecimal.ZERO);
        var result = underTest.process(selling(BigDecimal.valueOf(200), 200), context);

        assertThat(result.getTax()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(context.getLoss()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}