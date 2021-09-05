package antifraud.service.rules;

import antifraud.model.TransactionQueryRequest;
import antifraud.model.TransactionResponse;
import org.springframework.stereotype.Service;

import static antifraud.model.ResultEnum.MANUAL_PROCESSING;
import static antifraud.model.ResultEnum.PROHIBITED;

@Service
public class IpCountRule implements TransactionRule {
    private final static int MAX_ALLOWED = 2;
    private final static int MAX_MANUAL = 4;
    private static final String IP_COUNT_PROHIBITED_MESSAGE = "The number of IPs exceeded the maximum number";
    private static final String IP_COUNT_MANUALL_MESSAGE = "The number of IPs exceeded the maximum number";

    @Override
    public TransactionResponse getTransactionValidity(TransactionQueryRequest transactionRequest) {
        if (transactionRequest.getIpCount() > MAX_MANUAL) {
            return TransactionResponse
                    .builder()
                    .result(PROHIBITED)
                    .message(IP_COUNT_PROHIBITED_MESSAGE)
                    .build();
        }

        if (transactionRequest.getIpCount() > MAX_ALLOWED) {
            return TransactionResponse
                    .builder()
                    .result(MANUAL_PROCESSING)
                    .message(IP_COUNT_MANUALL_MESSAGE)
                    .build();
        }
        return TransactionResponse.allowedResponse();
    }
}
