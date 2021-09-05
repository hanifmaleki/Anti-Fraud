package antifraud.service;

import antifraud.exception.DataNotFoundException;
import antifraud.exception.InvalidDataException;
import antifraud.model.TransactionQueryRequest;
import antifraud.model.TransactionResponse;
import antifraud.service.rules.RuleEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TransactionService {

    private final RuleEngine ruleEngine;

    private final TransactionTypeService typeService;

    public TransactionResponse getTransactionValidity(TransactionQueryRequest transactionRequest) {
        String typeName = transactionRequest.getTransaction().getType();
        typeService.getTransactionTypeByName(typeName)
                .orElseThrow(() -> new DataNotFoundException("Could not find type " + typeName));
        return ruleEngine.getTransactionValidity(transactionRequest);
    }


}
