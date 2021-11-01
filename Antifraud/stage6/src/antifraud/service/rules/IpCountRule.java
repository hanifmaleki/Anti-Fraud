package antifraud.service.rules;

import antifraud.model.TransactionQuery;
import antifraud.model.TransactionResponse;
import org.springframework.stereotype.Service;

import static antifraud.model.ResultEnum.MANUAL_PROCESSING;
import static antifraud.model.ResultEnum.PROHIBITED;
import static java.util.Collections.singletonList;

@Service
public class IpCountRule implements TransactionRule {
    private final static int MAX_ALLOWED = 2;
    private final static int MAX_MANUAL = 4;
    private static final String IP_COUNT_PROHIBITED_MESSAGE = "The number of IPs exceeded the maximum number";
    private static final String IP_COUNT_MANUALL_MESSAGE = "The number of IPs exceeded the maximum number";

    @Override
    public TransactionResponse getTransactionValidity(TransactionQuery transactionQuery) {
        if (transactionQuery.getIpCount() > MAX_MANUAL) {
            return TransactionResponse
                    .builder()
                    .result(PROHIBITED)
                    .message(IP_COUNT_PROHIBITED_MESSAGE)
                    .notApplyingRules(singletonList(this))
                    .build();
        }

        if (transactionQuery.getIpCount() > MAX_ALLOWED) {
            return TransactionResponse
                    .builder()
                    .result(MANUAL_PROCESSING)
                    .message(IP_COUNT_MANUALL_MESSAGE)
                    .build();
        }
        return TransactionResponse.allowedResponse();
    }
}
