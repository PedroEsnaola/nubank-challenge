package br.com.nubank.presentation;

import br.com.nubank.application.FinanceOperationsCalculator;
import br.com.nubank.domain.model.FinanceOperation;
import br.com.nubank.domain.model.FinanceOperationResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class FinanceOperationCLI {

    private final ObjectMapper objectMapper;
    private final FinanceOperationsCalculator financeOperationsCalculator;

    public void start() throws JsonProcessingException {
        Scanner scanner = new Scanner(System.in).useDelimiter("\n");
        while (scanner.hasNext()){
            String line = scanner.nextLine();
            if (line.equals("exit") || line.isEmpty()) {
                return;
            }
            List<FinanceOperation> operations = objectMapper.readValue(line, new TypeReference<>() {});
            List<FinanceOperationResult> financeOperationResults = financeOperationsCalculator.processFinanceOperations(operations);
            System.out.println(objectMapper.writeValueAsString(financeOperationResults));
        }
    }
}
