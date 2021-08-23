package antifraud.service.rules;

import antifraud.model.ResultEnum;
import antifraud.model.Transaction;
import antifraud.model.TransactionQueryRequest;
import antifraud.model.TransactionResponse;
import antifraud.service.SuspiciousIpService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class IpCheckRule implements TransactionRule {

    private final String PROHIBITED_IP_MESSAGE = "The given IP address is suspicious.";


    private final SuspiciousIpService ipService;

    @Override
    public TransactionResponse getTransactionValidity(TransactionQueryRequest transactionRequest) {
        final Transaction transaction = transactionRequest.getTransaction();
        if (ipService.isSuspicious(transaction.getIpAddress())) {
            return TransactionResponse
                    .builder()
                    .result(ResultEnum.PROHIBITED)
                    .message(PROHIBITED_IP_MESSAGE)
                    .build();
        }

        return TransactionResponse
                .builder()
                .result(ResultEnum.ALLOWED)
                .build();
    }
}
