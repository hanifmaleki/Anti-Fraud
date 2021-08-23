package antifraud.service.rules;

import antifraud.model.TransactionQueryRequest;
import antifraud.model.TransactionResponse;

public interface TransactionRule {

    TransactionResponse getTransactionValidity(TransactionQueryRequest transactionRequest);
}
