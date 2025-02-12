package br.com.nubank.application.operator;

import br.com.nubank.domain.model.FinanceOperation;
import br.com.nubank.domain.model.FinanceOperationResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class SellFinanceOperationProcessorTest {

    SellFinanceOperationProcessor underTest = new SellFinanceOperationProcessor();

    @Test
    @DisplayName("Given a high profit on shares sale, when selling, should calculate tax correctly")
    void givenHighProfit_whenSellingShares_shouldCalculateTaxCorrectly() {

        FinanceOperation financeOperation = new FinanceOperation();
        financeOperation.setUnitCost(BigDecimal.valueOf(150));
        financeOperation.setQuantity(200);

        FinanceOperationContext context = new FinanceOperationContext();
        context.setAverageCost(BigDecimal.valueOf(100));
        context.setLoss(BigDecimal.ZERO);


        FinanceOperationResult result = underTest.process(financeOperation, context);


        assertThat(result.getTax()).isEqualByComparingTo(BigDecimal.valueOf(2000.00).setScale(2));
        assertThat(context.getLoss()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Given no profit, when selling shares, should calculate zero tax")
    void givenNoProfit_whenSellingShares_shouldCalculateZeroTax() {

        FinanceOperation financeOperation = new FinanceOperation();
        financeOperation.setUnitCost(BigDecimal.valueOf(100));
        financeOperation.setQuantity(50);

        FinanceOperationContext context = new FinanceOperationContext();
        context.setAverageCost(BigDecimal.valueOf(100));
        context.setLoss(BigDecimal.ZERO);


        FinanceOperationResult result = underTest.process(financeOperation, context);


        assertThat(result.getTax()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(context.getLoss()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Given a previous loss, when selling at profit, should calculate tax after deductible loss and dont pay taxed due to low profit")
    void givenPreviousLoss_whenSellingAtProfit_shouldDeductLossBeforeCalculatingTaxAndDontPayTaxesDueToLowProfit() {

        FinanceOperation financeOperation = new FinanceOperation();
        financeOperation.setUnitCost(BigDecimal.valueOf(150));
        financeOperation.setQuantity(100);

        FinanceOperationContext context = new FinanceOperationContext();
        context.setAverageCost(BigDecimal.valueOf(100));
        context.setLoss(BigDecimal.valueOf(2000));


        FinanceOperationResult result = underTest.process(financeOperation, context);


        assertThat(result.getTax()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(context.getLoss()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Given a loss, when selling shares, should update context with new loss")
    void givenALoss_whenSellingShares_shouldUpdateLossInContext() {

        FinanceOperation financeOperation = new FinanceOperation();
        financeOperation.setUnitCost(BigDecimal.valueOf(70));
        financeOperation.setQuantity(100);

        FinanceOperationContext context = new FinanceOperationContext();
        context.setAverageCost(BigDecimal.valueOf(100));
        context.setLoss(BigDecimal.valueOf(500));


        FinanceOperationResult result = underTest.process(financeOperation, context);


        assertThat(result.getTax()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(context.getLoss()).isEqualByComparingTo(BigDecimal.valueOf(3500));
    }

    @Test
    @DisplayName("Given high sale value but no profit, when selling shares, should calculate zero tax")
    void givenHighSaleValueNoProfit_whenSellingShares_shouldCalculateZeroTax() {

        FinanceOperation financeOperation = new FinanceOperation();
        financeOperation.setUnitCost(BigDecimal.valueOf(200));
        financeOperation.setQuantity(200);

        FinanceOperationContext context = new FinanceOperationContext();
        context.setAverageCost(BigDecimal.valueOf(200));
        context.setLoss(BigDecimal.valueOf(0));


        FinanceOperationResult result = underTest.process(financeOperation, context);


        assertThat(result.getTax()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(context.getLoss()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}