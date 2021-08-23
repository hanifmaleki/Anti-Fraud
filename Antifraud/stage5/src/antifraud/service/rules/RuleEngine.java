package antifraud.service.rules;

import antifraud.model.ResultEnum;
import antifraud.model.TransactionResponse;

public class RuleEngine {

    TransactionResponse mergerTransactionResult(TransactionResponse response1, TransactionResponse response2) {
        if (response1.getResult().equals(response2.getResult())) {
            return TransactionResponse
                    .builder()
                    .result(response1.getResult())
                    .message(String.format("%s\n%s", response1.getMessage(), response2.getMessage()))
                    .build();
        }

        return getGreaterResponse(response1, response2);
    }

    TransactionResponse getGreaterResponse(TransactionResponse response1, TransactionResponse response2) {
        if (response1.getResult() == response1.getResult()) {
            throw new RuntimeException("Could not find greater in two equal response");
        }

        final boolean response1IsLowest = response1.getResult() == ResultEnum.ALLOWED;
        final boolean response2IsGreatest = response2.getResult() == ResultEnum.PROHIBITED;

        if (response1IsLowest || response2IsGreatest) {
            return response2;
        }

        return response1;

    }
}
