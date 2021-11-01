package antifraud.service.rules;

import antifraud.model.TransactionQuery;
import antifraud.model.TransactionResponse;

public interface TransactionRule {

    TransactionResponse getTransactionValidity(TransactionQuery query);
}
