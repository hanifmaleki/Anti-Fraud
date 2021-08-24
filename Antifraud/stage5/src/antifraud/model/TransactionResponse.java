package antifraud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {

    private final static String ALLOWED_MESSAGE = "The transaction is allowed.";

    private ResultEnum result;
    private String message;

    public static TransactionResponse allowedResponse() {
        return TransactionResponse
                .builder()
                .result(ResultEnum.ALLOWED)
                .message(ALLOWED_MESSAGE)
                .build();
    }
}
