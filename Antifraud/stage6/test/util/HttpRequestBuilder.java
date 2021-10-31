package util;

import antifraud.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.UnexpectedResultException;
import hassured.HMatcher;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.testcase.CheckResult;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.HttpRequestType.*;


public class HttpRequestBuilder<T> {

    private User user = null;
    private Map<String, String> queryParams = new HashMap<>();
    private final BaseRestTestUtil<T> util;

    List<HMatcher<String>> bodyMatchers = new ArrayList<>();
    private Integer expectedStatus = null;
    private HttpRequestType type;
    private Object parameter1;
    private String errorMessage = null;

    private ObjectMapper objectMapper = new ObjectMapper();

    private HttpRequestBuilder(BaseRestTestUtil<T> util) {
        this.util = util;
    }

    public static <T> HttpRequestBuilder<T> builder(BaseRestTestUtil<T> util) {
        return new HttpRequestBuilder(util);
    }


    public HttpRequestBuilder<T> withUser(User user) {
        this.user = user;
        return this;
    }

    public HttpRequestBuilder<T> withErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public HttpRequestBuilder<T> queryParam(String name, String value){
        this.queryParams.put(name, value);
        return this;
    }


    public HttpRequestBuilder<T> whenGetOne(String identifier) {
        this.type = GET_ONE;
        this.parameter1 = identifier;
        return this;
    }


    public HttpRequestBuilder<T> whenAdd(T element) {
        this.type = ADD;
        this.parameter1 = element;
        return this;
    }


    public HttpRequestBuilder<T> whenRemove(String identifier) {
        this.type = DELETE;
        this.parameter1 = identifier;
        return this;
    }

    public HttpRequestBuilder<T> whenGetList() {
        this.type = GET_LIST;
        return this;
    }

    public HttpRequestBuilder<T> then() {
        return this;
    }

    public HttpRequestBuilder<T> statusCode(int statusCode) {
        this.expectedStatus = statusCode;
        return this;
    }


    public String checkAndReturnContent() {
        final HttpResponse response = httpResponse();
        if (expectedStatus != null) {
            final int receivedStatusCode = response.getStatusCode();
            if (expectedStatus != receivedStatusCode) {
                final String feedback = getFeedbackMessage(response, receivedStatusCode);
                CheckResult wrong = CheckResult.wrong(feedback);
                throw new UnexpectedResultException(wrong);
            }
        }
        return response.getContent();
    }

    private String getFeedbackMessage(HttpResponse response, int receivedStatusCode) {
        final String defaultFeedback = String.format("expect %d, but received %d from the request %s",
                expectedStatus,
                receivedStatusCode,
                response.getRequest().getUri());
        final String feedback = this.errorMessage == null ? "" : errorMessage;
        return String.format("%s. %s", feedback, defaultFeedback);
    }

    public HttpRequest httpRequest() {
        switch (this.type) {
            case GET_LIST:
                return getList();
            case GET_ONE:
                return getOne((String) parameter1);
            case ADD:
                return add((T) parameter1);
            case DELETE:
                return delete((String) parameter1);
            default:
                throw new RuntimeException("Type is not specified");
        }

    }

    public HttpResponse httpResponse() {
        return httpRequest().send();
    }


    private Map<String, String> getAuthorizationHeader(User user) {
        String auth = user.getUsername() + ":" + user.getPassword();
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);

        Map<String, String> authParameter = new HashMap<>();
        authParameter.put("Authorization", authHeader);
        return authParameter;
    }


    private void processHeader(HttpRequest httpRequest) {
        if (user != null) {
            httpRequest.addHeaders(getAuthorizationHeader(user));
        }
        queryParams.forEach(httpRequest::addHeader);
    }

    private HttpRequest getList() {
        final HttpRequest httpRequest = util.getTestClass().get(util.getBaseAddress());
        processHeader(httpRequest);
        return httpRequest;
    }

    private HttpRequest getOne(String id) {
        String path = util.getBaseAddress() + "/" + id;
        final HttpRequest httpRequest = util.getTestClass().get(path);
        processHeader(httpRequest);
        return httpRequest;
    }

    private HttpRequest delete(String id) {
        String path = util.getBaseAddress() + "/" + id;
        final HttpRequest httpRequest = util.getTestClass().delete(path);
        processHeader(httpRequest);
        return httpRequest;
    }

    private HttpRequest add(T body) {
        final String bodyInString = toJson(body);
        final HttpRequest httpRequest = util.getTestClass().post(util.getBaseAddress(), bodyInString);
        processHeader(httpRequest);
        return httpRequest;
    }

    private String toJson(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /*public HttpRequestBuilder<T> body(HMatcher<String> matcher) {
        bodyMatchers.add(matcher);
        return this;
    }*/


}
