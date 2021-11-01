package antifraud.model;

import antifraud.service.rules.TransactionRule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {
    private final static String ALLOWED_MESSAGE = "The transaction is allowed.";

    private long id;
    private ResultEnum result;
    private String message;
    private List<TransactionRule> notApplyingRules = new ArrayList<>();

    public static TransactionResponse allowedResponse() {
        return TransactionResponse
                .builder()
                .result(ResultEnum.ALLOWED)
                .message(ALLOWED_MESSAGE)
                .build();
    }
}
