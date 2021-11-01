package antifraud.service.rules;

import antifraud.model.ResultEnum;
import antifraud.model.Transaction;
import antifraud.model.TransactionQuery;
import antifraud.model.TransactionResponse;
import antifraud.service.SuspiciousIpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IpCheckRule implements TransactionRule {

    private final String PROHIBITED_IP_MESSAGE = "The given IP address is suspicious.";


    private final SuspiciousIpService ipService;

    @Override
    public TransactionResponse getTransactionValidity(TransactionQuery transactionQuery) {
        final Transaction transaction = transactionQuery.getTransaction();
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
