package antifraud.service.rules;

import antifraud.exception.DataNotFoundException;
import antifraud.model.*;
import antifraud.service.TransactionTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.util.Collections.singletonList;

@Service
@RequiredArgsConstructor
public class TransactionAmountRule implements TransactionRule {

    private final TransactionTypeService transactionTypeService;

    private final String MANUAL_MESSAGE = "The transactions with this price needs to be confirmed manually.";
    private final String PROHIBITED_AMOUNT_MESSAGE = "The transaction is above allowed value.";

    @Override
    public TransactionResponse getTransactionValidity(TransactionQuery transactionQuery) {
        final Transaction transaction = transactionQuery.getTransaction();
        final String typeName = transaction.getType();
        final TransactionType transactionType = transactionTypeService.getTransactionTypeByName(typeName)
                .orElseThrow(() -> new DataNotFoundException("There is no registered transaction type with name " + typeName));
        int amount = transaction.getAmount();
        if (amount <= transactionType.getCurrentMaxAllowed()) {
            return TransactionResponse.allowedResponse();

        }

        if (amount <= transactionType.getCurrentMaxManual()) {
            return TransactionResponse.builder()
                    .result(ResultEnum.MANUAL_PROCESSING)
                    .message(MANUAL_MESSAGE)
                    .notApplyingRules(singletonList(this))
                    .build();
        }

        return TransactionResponse.builder()
                .result(ResultEnum.PROHIBITED)
                .message(PROHIBITED_AMOUNT_MESSAGE)
                .notApplyingRules(singletonList(this))
                .build();

    }
}
