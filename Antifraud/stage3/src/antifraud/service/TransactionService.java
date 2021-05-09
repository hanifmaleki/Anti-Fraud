package antifraud.service;

import antifraud.model.ResultEnum;
import antifraud.model.Transaction;
import antifraud.model.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TransactionService {

    private static final long MAX_ALLOWED = 200L;
    private static final long MAX_MANUALL = 1500L;

    private final String ALLOWED_MESSAGE = "The transaction is allowed.";
    private final String MANUAL_MESSAGE = "The transactions with this price needs to be confirmed manually.";
    private final String PROHIBITED_AMOUNT_MESSAGE = "The transaction is above allowed value.";
    private final String PROHIBITED_CARD_MESSAGE = "The given card is in the blacklist.";
    private final String PROHIBITED_IP_MESSAGE = "The given IP address is suspicious.";

    private final StolenCardService cardService;
    private final SuspiciousIpService ipService;


    public TransactionResponse getTransactionValidity(Transaction transaction) {

        Long amount = Long.valueOf(transaction.getAmount());
        TransactionResponse result = getResultByAmount(amount);

        if (cardService.isBlacklist(transaction.getCardSerial())) {
            if (result.getResult() != ResultEnum.PROHIBITED) {
                result.setResult(ResultEnum.PROHIBITED);
                result.setMessage(PROHIBITED_CARD_MESSAGE);
            } else {
                result.setMessage(result.getMessage() + "\n" + PROHIBITED_CARD_MESSAGE);
            }
        }

        if(ipService.isSuspicious(transaction.getIpAddress())) {
            if (result.getResult() != ResultEnum.PROHIBITED) {
                result.setResult(ResultEnum.PROHIBITED);
                result.setMessage(PROHIBITED_IP_MESSAGE);
            } else {
                result.setMessage(result.getMessage() + "\n" + PROHIBITED_IP_MESSAGE);
            }
        }

        return result;
    }

    private TransactionResponse getResultByAmount(Long amount) {
        if (amount <= MAX_ALLOWED) {
            return TransactionResponse.builder()
                    .result(ResultEnum.ALLOWED)
                    .message(ALLOWED_MESSAGE)
                    .build();
        }

        if (amount <= MAX_MANUALL) {
            return TransactionResponse.builder()
                    .result(ResultEnum.MANUAL_PROCESSING)
                    .message(MANUAL_MESSAGE)
                    .build();
        }

        return TransactionResponse.builder()
                .result(ResultEnum.PROHIBITED)
                .message(PROHIBITED_AMOUNT_MESSAGE)
                .build();
    }

}
