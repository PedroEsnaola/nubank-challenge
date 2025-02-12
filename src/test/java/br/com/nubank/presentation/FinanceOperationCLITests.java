package br.com.nubank.presentation;

import br.com.nubank.application.FinanceOperationsCalculator;
import br.com.nubank.application.operator.BuyFinanceOperationProcessor;
import br.com.nubank.application.operator.FinanceOperatorFactory;
import br.com.nubank.application.operator.SellFinanceOperationProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static com.github.stefanbirkner.systemlambda.SystemLambda.withTextFromSystemIn;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

public class FinanceOperationCLITests {

    FinanceOperationCLI underTest = new FinanceOperationCLI(new ObjectMapper(), new FinanceOperationsCalculator(new FinanceOperatorFactory(List.of(new BuyFinanceOperationProcessor(), new SellFinanceOperationProcessor()))));

    @Test
    void testConsoleInputAndOutput() throws Exception {



        // Simulação de entradas via System.in
        String input1 = "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},  {\"operation\":\"sell\", \"unit-cost\":50.00, \"quantity\": 10000},  {\"operation\":\"buy\", \"unit-cost\":20.00, \"quantity\": 10000}, {\"operation\":\"sell\", \"unit-cost\":50.00, \"quantity\": 10000}]";
        String input2 = "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},  {\"operation\":\"sell\", \"unit-cost\":2.00, \"quantity\": 5000},  {\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000},  {\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000},  {\"operation\":\"sell\", \"unit-cost\":25.00, \"quantity\": 1000},  {\"operation\":\"buy\", \"unit-cost\":20.00, \"quantity\": 10000},  {\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 5000},  {\"operation\":\"sell\", \"unit-cost\":30.00, \"quantity\": 4350},  {\"operation\":\"sell\", \"unit-cost\":30.00, \"quantity\": 650}]";
        String input3 = "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},  {\"operation\":\"sell\", \"unit-cost\":2.00, \"quantity\": 5000},  {\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000},  {\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000},  {\"operation\":\"sell\", \"unit-cost\":25.00, \"quantity\": 1000}]";
        // Usando SystemLambda para simular entradas + capturar saídas
        String output = tapSystemOut(() -> {
            // Executa o código principal da aplicação
            withTextFromSystemIn(input1,input2, input3, "exit")
                    .execute(() -> {
                        underTest.start();
                    });
        });

        // Validações na saída do console
        assertThat(output)
                .contains("[{\"tax\":0.00},{\"tax\":80000.00},{\"tax\":0.00},{\"tax\":60000.00}]")
                .contains("[{\"tax\":0.00},{\"tax\":0.00},{\"tax\":0.00},{\"tax\":0.00},{\"tax\":3000.00},{\"tax\":0.00},{\"tax\":0.00},{\"tax\":3700.00},{\"tax\":0.00}]")
                .contains("[{\"tax\":0.00},{\"tax\":0.00},{\"tax\":0.00},{\"tax\":0.00},{\"tax\":3000.00}]");// Verifica saída 1
    }
}

