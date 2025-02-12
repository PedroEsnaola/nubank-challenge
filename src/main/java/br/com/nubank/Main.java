package br.com.nubank;

import br.com.nubank.application.FinanceOperationsCalculator;
import br.com.nubank.application.operator.BuyFinanceOperationProcessor;
import br.com.nubank.application.operator.FinanceOperationProcessorFactory;
import br.com.nubank.application.operator.SellFinanceOperationProcessor;
import br.com.nubank.presentation.FinanceOperationCLI;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.util.List;

public class Main {

    public static void main(String[] args) throws JsonProcessingException {
        FinanceOperationProcessorFactory financeOperationProcessorFactory = new FinanceOperationProcessorFactory(List.of(new BuyFinanceOperationProcessor(), new SellFinanceOperationProcessor()));
        ObjectMapper objectMapper = JsonMapper.builder().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS).build();
        new FinanceOperationCLI(objectMapper, new FinanceOperationsCalculator(financeOperationProcessorFactory)).start();
    }
}