package util;

import antifraud.model.User;
import exception.UnexpectedResultException;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.testcase.CheckResult;
import org.springframework.http.HttpStatus;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static data.IpAndCardDataProvider.IP_ADDRESS;
import static hassured.HMatchers.assertThat;
import static hassured.HMatchers.hasSize;

public class IPTestUtil extends BaseRestTestUtil<String> {


    public IPTestUtil(AntifraudBaseTest testClass) {
        super(testClass, String.class);
    }


    public void addSuspiciousIpAndExpectStatus(User user, String ip, HttpStatus expectedStatus) {
        Map<String, String> params = new HashMap<>();
        params.put("ip", ip);
        HttpRequest postRequest = testClass.post(IP_ADDRESS, params)
                .addHeaders(testClass.getAuthorizationHeader(user));
        sendPostRequestAndExpect(ip, expectedStatus, postRequest);
    }

    public List<String> getSuspiciousIpsAndExpectSize(int expectedSize) {
        final List<String> list = this.get();
        assertThat(list, hasSize(expectedSize));
        return list;
    }

    public void deleteSuspiciousIpAndExpect(User user, String ip, HttpStatus expectedStatus) {
        this.sendDeleteAndExpect(ip, expectedStatus.value(), "");
    }

    public boolean isAuthorizedIpGet(User user) {
        HttpRequest getRequest = testClass.get(IP_ADDRESS)
                .addHeaders(testClass.getAuthorizationHeader(user));
        final HttpResponse response = getRequest.send();
        final int responseCode = response.getStatusCode();
        if (responseCode == HttpStatus.FORBIDDEN.value()) {
            return false;
        }
        if (responseCode == HttpStatus.OK.value()) {
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



    @Override
    public String getBaseAddress() {
        return "/suspicious-ip";
    }

    @Override
    protected String getEntityName() {
        return "Suspicious IP";
    }

    @Override
    public Function<String, String> getIdentifier() {
        return String::toString;
    }
}
