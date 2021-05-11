import antifraud.model.ResultEnum;
import antifraud.model.Transaction;
import antifraud.model.TransactionResponse;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionTestUtil extends BaseTestUtil {
    public TransactionTestUtil(SpringTest testClass) {
        super(testClass);
    }

    private void queryTrxAndExpect(Transaction transaction, ResultEnum expectedResult, String... expectedMessageWords) {
        final String trxJson = toJson(transaction);
        HttpRequest getRequest = testClass.post(TestDataProvider.TRX_ADDRESS, trxJson);
        HttpResponse response = getRequest.send();
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
}
