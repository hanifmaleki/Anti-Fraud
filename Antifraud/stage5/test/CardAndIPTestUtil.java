import antifraud.model.User;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.testcase.CheckResult;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static data.IpAndCardDataProvider.IP_ADDRESS;
import static data.IpAndCardDataProvider.STOLEN_ADDRESS;
import static org.springframework.http.HttpStatus.OK;

public class CardAndIPTestUtil extends BaseTestUtil {


    public CardAndIPTestUtil(AntifraudBaseTest testClass) {
        super(testClass);
    }

    private void addStolenCardAndExpectStatus(String serialNumber, HttpStatus expectedStatus) {
        Map<String, String> params = new HashMap<>();
        params.put("serialNumber", serialNumber);
        HttpRequest postRequest = testClass.post(STOLEN_ADDRESS, params);
        sendPostRequestAndExpect(serialNumber, expectedStatus, postRequest);
    }


    public List<String> getStolenCardsAndExpectSize(int expectedSize) {
        HttpRequest getRequest = testClass.get(STOLEN_ADDRESS)
                .addHeaders(testClass.getDefaultAdminAuthorization());
        return getListOfStringWithExpectedSize(expectedSize, getRequest);
    }

    public void deleteCardAndExpect(User user, String serialNumber, HttpStatus expectedStatus) {
        HttpRequest deleteRequest = testClass.delete(STOLEN_ADDRESS + "/" + serialNumber)
                .addHeaders(testClass.getAuthorizationHeader(user));
        sendDeleteRequestAndExpect(serialNumber, expectedStatus, deleteRequest);

    }

    public boolean isAuthorizedCardGet(User user){
        HttpRequest getRequest = testClass.get(STOLEN_ADDRESS)
                .addHeaders(testClass.getAuthorizationHeader(user));
        final HttpResponse response = getRequest.send();
        final int responseCode = response.getStatusCode();
        if(responseCode == HttpStatus.FORBIDDEN.value()){
            return false;
        }
        if(responseCode == HttpStatus.OK.value()){
            return true;
        }
        final String feedback = String.format("Unexpected status %s received from get cards call", response);
        throw new UnexpectedResultException(CheckResult.wrong(feedback));
    }

    public void addSuspiciousIpAndExpectStatus(User user, String ip, HttpStatus expectedStatus) {
        Map<String, String> params = new HashMap<>();
        params.put("ip", ip);
        HttpRequest postRequest = testClass.post(IP_ADDRESS, params)
                .addHeaders(testClass.getAuthorizationHeader(user));
        sendPostRequestAndExpect(ip, expectedStatus, postRequest);
    }

    public List<String> getSuspiciousIpsAndExpectSize(int expectedSize) {
        HttpRequest getRequest = testClass.get(IP_ADDRESS)
                .addHeaders(testClass.getDefaultAdminAuthorization());
        return getListOfStringWithExpectedSize(expectedSize, getRequest);
    }

    public void deleteSuspiciousIpAndExpect(User user, String ip, HttpStatus expectedStatus) {
        HttpRequest deleteRequest = testClass.delete(IP_ADDRESS + "/" + ip)
                .addHeaders(testClass.getAuthorizationHeader(user));
        sendDeleteRequestAndExpect(ip, expectedStatus, deleteRequest);
    }

    public boolean isAuthorizedIpGet(User user){
        HttpRequest getRequest = testClass.get(IP_ADDRESS)
                .addHeaders(testClass.getAuthorizationHeader(user));
        final HttpResponse response = getRequest.send();
        final int responseCode = response.getStatusCode();
        if(responseCode == HttpStatus.FORBIDDEN.value()){
            return false;
        }
        if(responseCode == HttpStatus.OK.value()){
            return true;
        }
        final String feedback = String.format("Unexpected status %s received from get IP call", response);
        throw new UnexpectedResultException(CheckResult.wrong(feedback));
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
