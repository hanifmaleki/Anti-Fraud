package util;

import antifraud.model.User;
import exception.UnexpectedResultException;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.testcase.CheckResult;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static data.IpAndCardDataProvider.IP_ADDRESS;
import static data.IpAndCardDataProvider.STOLEN_ADDRESS;
import static hassured.HMatchers.assertThat;
import static hassured.HMatchers.hasSize;

public class CardTestUtil extends BaseRestTestUtil<String> {


    public CardTestUtil(AntifraudBaseTest testClass) {
        super(testClass, String.class);
    }

    private void addStolenCardAndExpectStatus(String serialNumber, HttpStatus expectedStatus) {
        Map<String, String> params = new HashMap<>();
        params.put("serialNumber", serialNumber);
        HttpRequest postRequest = testClass.post(STOLEN_ADDRESS, params);
        sendPostRequestAndExpect(serialNumber, expectedStatus, postRequest);
    }


    public List<String> getStolenCardsAndExpectSize(int expectedSize) {
        final List<String> list = get();
        assertThat(list, hasSize(expectedSize));
        return list;
    }

    public void deleteCardAndExpect(User user, String serialNumber, HttpStatus expectedStatus) {
        this.sendDeleteAndExpect(serialNumber, user, expectedStatus.value(), "");
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

    @Override
    public String getBaseAddress() {
        return "/stolencard";
    }

    @Override
    protected String getEntityName() {
        return "Stolen Card";
    }

    @Override
    public Function<String, String> getIdentifier() {
        return String::toString;
    }
}
