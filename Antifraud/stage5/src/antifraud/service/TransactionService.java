package antifraud.service;

import antifraud.model.TransactionQueryRequest;
import antifraud.model.TransactionResponse;
import antifraud.service.rules.RuleEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TransactionService {

    private  final RuleEngine ruleEngine;

    public TransactionResponse getTransactionValidity(TransactionQueryRequest transactionRequest) {
        return ruleEngine.getTransactionValidity(transactionRequest);
    }


}
