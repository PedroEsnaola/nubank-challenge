package br.com.nubank;

import br.com.nubank.application.FinanceOperationsCalculator;
import br.com.nubank.application.operator.BuyFinanceOperationProcessor;
import br.com.nubank.application.operator.FinanceOperatorFactory;
import br.com.nubank.application.operator.SellFinanceOperationProcessor;
import br.com.nubank.presentation.FinanceOperationCLI;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws JsonProcessingException {
        FinanceOperatorFactory financeOperatorFactory = new FinanceOperatorFactory(List.of(new BuyFinanceOperationProcessor(), new SellFinanceOperationProcessor()));
        new FinanceOperationCLI(new ObjectMapper(), new FinanceOperationsCalculator(financeOperatorFactory)).start();
    }
}