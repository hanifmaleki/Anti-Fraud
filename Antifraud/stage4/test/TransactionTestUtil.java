import antifraud.model.ResultEnum;
import antifraud.model.Transaction;
import antifraud.model.TransactionResponse;
import antifraud.model.User;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.testcase.CheckResult;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionTestUtil extends BaseTestUtil {
    public TransactionTestUtil(AntifraudBaseTest testClass) {
        super(testClass);
    }

    public void queryTrxAndExpectResultEnum(User user, Transaction transaction, ResultEnum expectedResult, String... expectedMessageWords) {
        HttpResponse response = addQueryAndGetResponse(user, transaction);
        TransactionResponse transactionResponse = fromJson(response, TransactionResponse.class);

        ResultEnum receivedResult = transactionResponse.getResult();
        if (receivedResult != expectedResult) {
            String feedback = String.format("The transaction %s result is %s but expected to be %s",
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
    }

    public void queryTrxAndExpectUnauthorizedHttpStatus(User user, Transaction transaction) {
        HttpResponse response = addQueryAndGetResponse(user, transaction);
        final int responseCode = response.getStatusCode();

        if(responseCode != HttpStatus.UNAUTHORIZED.value()){
            String feedback = String.format("Expected unauthorized status but received %d", responseCode);
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
    }

    public boolean isUserAuthorizedForTrxQuery(User user, Transaction transaction) {
        HttpResponse response = addQueryAndGetResponse(user, transaction);
        final int responseCode = response.getStatusCode();
        if(responseCode == HttpStatus.FORBIDDEN.value()){
            return false;
        }
        if(responseCode == HttpStatus.OK.value()){
            return true;
        }
        final String feedback = String.format("Unexpected status %s received from Transaction query call", response);
        throw new UnexpectedResultException(CheckResult.wrong(feedback));
    }


    private HttpResponse addQueryAndGetResponse(User user, Transaction transaction) {
        final String trxJson = toJson(transaction);
        HttpRequest getRequest = testClass.post(TestDataProvider.TRX_ADDRESS, trxJson)
                .addHeaders(testClass.getAuthorizationHeader(user));
        return getRequest.send();
    }
}
