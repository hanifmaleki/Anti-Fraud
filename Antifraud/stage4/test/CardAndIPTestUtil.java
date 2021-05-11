import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

public class CardAndIPTestUtil extends BaseTestUtil {


    public CardAndIPTestUtil(SpringTest testClass) {
        super(testClass);
    }

    private void addStolenCardAndExpectStatus(String serialNumber, HttpStatus expectedStatus) {
        Map<String, String> params = new HashMap<>();
        params.put("serialNumber", serialNumber);
        HttpRequest postRequest = testClass.post(TestDataProvider.STOLEN_ADDRESS, params);
        sendPostRequestAndExpect(serialNumber, expectedStatus, postRequest);
    }


    private List<String> getStolenCardsAndExpectSize(int expectedSize) {
        HttpRequest getRequest = testClass.get(TestDataProvider.STOLEN_ADDRESS);
        return getListOfStringWithExpectedSize(expectedSize, getRequest);
    }

    private void deleteCardAndExpect(String serialNumber, HttpStatus expectedStatus) {
        HttpRequest deleteRequest = testClass.delete(TestDataProvider.STOLEN_ADDRESS + "/" + serialNumber);
        sendDeleteRequestAndExpect(serialNumber, expectedStatus, deleteRequest);

    }

    private void addSuspiciousIpAndExpectStatus(String ip, HttpStatus expectedStatus) {
        Map<String, String> params = new HashMap<>();
        params.put("ip", ip);
        HttpRequest postRequest = testClass.post(TestDataProvider.IP_ADDRESS, params);
        sendPostRequestAndExpect(ip, expectedStatus, postRequest);
    }

    private List<String> getSuspiciousIpsAndExpectSize(int expectedSize) {
        HttpRequest getRequest = testClass.get(TestDataProvider.IP_ADDRESS);
        return getListOfStringWithExpectedSize(expectedSize, getRequest);
    }

    private void deleteSuspiciousIpAndExpect(String ip, HttpStatus expectedStatus) {
        HttpRequest deleteRequest = testClass.delete(TestDataProvider.IP_ADDRESS + "/" + ip);
        System.out.println(deleteRequest.getLocalUri());
        sendDeleteRequestAndExpect(ip, expectedStatus, deleteRequest);
    }

    private List<String> getListOfStringWithExpectedSize(int expectedSize, HttpRequest getRequest) {
        HttpResponse httpResponse = getRequest.send();
        int receivedCode = httpResponse.getStatusCode();
        if (receivedCode != OK.value()) {
            String feedback = String.format("Unexpected get result %d", receivedCode);
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
        List<String> list = Arrays.asList(fromJson(httpResponse, String[].class));
        int receivedSize = list.size();
        if (receivedSize != expectedSize) {
            String feedback = String.format("The expected list size is %d, however received %d", expectedSize, receivedSize);
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
        return list;
    }

    private void sendPostRequestAndExpect(String serialNumber, HttpStatus expectedStatus, HttpRequest postRequest) {
        HttpResponse httpResponse = postRequest.send();
        int expectedCode = expectedStatus.value();
        int receivedCode = httpResponse.getStatusCode();
        if (receivedCode != expectedCode) {
            String feedback = String.format("Expected status %d after adding item %s but received status %d for request:\n%s",
                    expectedCode, serialNumber, receivedCode, postRequest.getUri());
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
    }

    private void sendDeleteRequestAndExpect(String serialNumber, HttpStatus expectedStatus, HttpRequest deleteRequest) {
        HttpResponse response = deleteRequest.send();

        int statusCode = response.getStatusCode();
        int expectedValue = expectedStatus.value();
        if (statusCode != expectedValue) {
            String feedback = String.format("Delete operation of %s expect code %d but received code %d"
                    , serialNumber, expectedValue, statusCode);
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
    }

    private void checkItemExistInList(List<String> list, String item) {
        if (!list.contains(item)) {
            String feedback = String.format("Can not find item %s in the list", item);
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
    }
}
