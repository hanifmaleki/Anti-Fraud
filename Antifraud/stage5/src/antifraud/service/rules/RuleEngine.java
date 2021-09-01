package antifraud.service.rules;

import antifraud.model.ResultEnum;
import antifraud.model.TransactionQueryRequest;
import antifraud.model.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static antifraud.model.ResultEnum.ALLOWED;

@Service
public class RuleEngine {


    private final List<TransactionRule> rules;

    @Autowired
    public RuleEngine(List<TransactionRule> rules) {
        this.rules = rules;
    }

    public TransactionResponse getTransactionValidity(TransactionQueryRequest queryRequest) {
        final TransactionResponse response = rules
                .stream()
                .map(rule -> rule.getTransactionValidity(queryRequest))
                .reduce(TransactionResponse.allowedResponse(), (a, b) -> mergerTransactionResult(a, b));

        return response;
    }

    //TODO can move to TransactionResponse
    private TransactionResponse mergerTransactionResult(TransactionResponse response1, TransactionResponse response2) {
        if (response1 == null) {
            return response2;
        }
        if (response2 == null) {
            return response1;
        }

        if (response1.getResult().equals(response2.getResult())) {
            String message = response1.getResult()==ALLOWED ? response1.getMessage() : String.format("%s\n%s", response1.getMessage(), response2.getMessage());
            return TransactionResponse
                    .builder()
                    .result(response1.getResult())
                    .message(message)
                    .build();
        }

        return getGreaterResponse(response1, response2);
    }


    private TransactionResponse getGreaterResponse(TransactionResponse response1, TransactionResponse response2) {
        if (response1.getResult() == response2.getResult()) {
            throw new RuntimeException("Could not find greater in two equal response");
        }

        final boolean response1IsLowest = response1.getResult() == ALLOWED;
        final boolean response2IsGreatest = response2.getResult() == ResultEnum.PROHIBITED;

        if (response1IsLowest || response2IsGreatest) {
            return response2;
        }

        return response1;

    }




}