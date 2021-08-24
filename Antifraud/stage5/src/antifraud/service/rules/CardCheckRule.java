package antifraud.service.rules;

import antifraud.model.ResultEnum;
import antifraud.model.Transaction;
import antifraud.model.TransactionQueryRequest;
import antifraud.model.TransactionResponse;
import antifraud.service.StolenCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardCheckRule implements TransactionRule {
    private final String PROHIBITED_CARD_MESSAGE = "The given card is in the blacklist.";

    private final StolenCardService cardService;

    @Override
    public TransactionResponse getTransactionValidity(TransactionQueryRequest transactionRequest) {
        final Transaction transaction = transactionRequest.getTransaction();

        if (cardService.isBlacklist(transaction.getCardSerial())) {
            TransactionResponse
                    .builder()
                    .result(ResultEnum.PROHIBITED)
                    .message(PROHIBITED_CARD_MESSAGE)
                    .build();
        }

        return TransactionResponse
                .builder()
                .result(ResultEnum.ALLOWED)
                .build();
    }
}
