package util;

import antifraud.model.ResultEnum;
import antifraud.model.Transaction;
import antifraud.model.TransactionResponse;
import antifraud.model.User;
import exception.UnexpectedResultException;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.testcase.CheckResult;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransactionTestUtil extends BaseRestTestUtil<Transaction> {
    public TransactionTestUtil(AntifraudBaseTest testClass) {
        super(testClass, Transaction.class);
    }

    public long queryTrxAndExpectResultEnum(User user, Transaction transaction, ResultEnum expectedResult, String... expectedMessageWords) {
        HttpResponse response = addQueryAndGetResponse(user, transaction);
        TransactionResponse transactionResponse = fromJson(response, TransactionResponse.class);

        ResultEnum receivedResult = transactionResponse.getResult();
        if (receivedResult != expectedResult) {
            String feedback = String.format("The transactionRequest %s result is %s but expected to be %s",
                    transaction.toString(), receivedResult, expectedResult);
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }

        String message = transactionResponse.getMessage();
        List<String> missingWords = Arrays.stream(expectedMessageWords)
                .map(String::toLowerCase)
                .filter(word -> !message.contains(word.toLowerCase()))
                .collect(Collectors.toList());
        if (!missingWords.isEmpty()) {
            String feedback = missingWords.stream()
                    .collect(Collectors.joining("\n", "The result message is missing the following phrase(s):\n", ""));
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
        return transactionResponse.getId();
    }

    public long queryTrxAndExpectUnauthorizedHttpStatus(User user, Transaction transaction) {
        HttpResponse response = addQueryAndGetResponse(user, transaction);
        final int responseCode = response.getStatusCode();

        if (responseCode != HttpStatus.UNAUTHORIZED.value()) {
            String feedback = String.format("Expected unauthorized status but received %d", responseCode);
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
        TransactionResponse transactionResponse = fromJson(response, TransactionResponse.class);
        return transactionResponse.getId();
    }


    public boolean isUserAuthorizedForTrxQuery(User user, Transaction transaction) {
        final HttpResponse response = HttpRequestBuilder.builder(this)
                .withUser(user)
                .whenAdd(transaction)
                .httpResponse();

        final int responseCode = response.getStatusCode();
        if (responseCode == HttpStatus.FORBIDDEN.value()) {
            return false;
        }
        if (responseCode == HttpStatus.OK.value()) {
            return true;
        }
        final String feedback = String.format("Unexpected status %s received from Transaction query call", response);
        throw new UnexpectedResultException(CheckResult.wrong(feedback));
    }


    private HttpResponse addQueryAndGetResponse(User user, Transaction trxRequest) {
        return HttpRequestBuilder.builder(this)
                .withUser(user)
                .whenAdd(trxRequest)
                .httpResponse();
    }

    @Override
    public String getBaseAddress() {
        return "transaction";
    }

    @Override
    protected String getEntityName() {
        return "Transaction";
    }

    @Override
    public Function<Transaction, String> getIdentifier() {
        return (transaction -> transaction.getId().toString());
    }
}
