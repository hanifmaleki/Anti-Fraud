package antifraud.service.rules;

import antifraud.model.TransactionQueryRequest;
import antifraud.model.TransactionResponse;
import org.springframework.stereotype.Service;

import static antifraud.model.ResultEnum.MANUAL_PROCESSING;
import static antifraud.model.ResultEnum.PROHIBITED;

@Service
public class CountryCountRule implements TransactionRule {
    private final static int MAX_ALLOWED = 2;
    private final static int MAX_MANUAL = 3;
    private static final String COUNTRY_COUNT_PROHIBITED_MESSAGE = "The number of countries exceeded the maximum number";
    private static final String COUNTRY_COUNT_MANUALL_MESSAGE = "The number of countries exceeded the maximum number";

    @Override
    public TransactionResponse getTransactionValidity(TransactionQueryRequest transactionRequest) {
        if (transactionRequest.getCountryCount() > MAX_MANUAL) {
            return TransactionResponse
                    .builder()
                    .result(PROHIBITED)
                    .message(COUNTRY_COUNT_PROHIBITED_MESSAGE)
                    .build();
        }

        if (transactionRequest.getCountryCount() > MAX_ALLOWED) {
            return TransactionResponse
                    .builder()
                    .result(MANUAL_PROCESSING)
                    .message(COUNTRY_COUNT_MANUALL_MESSAGE)
                    .build();
        }
        return TransactionResponse.allowedResponse();
    }
}
