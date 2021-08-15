import antifraud.model.User;
import org.assertj.swing.junit.dependency.commons_codec.binary.Base64;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public abstract class AntifraudBaseTest extends SpringTest {


    public AntifraudBaseTest(Class<?> clazz) {
        super(clazz);
    }

    public AntifraudBaseTest(Class<?> clazz, int port) {
        super(clazz, port);
    }

    public AntifraudBaseTest(Class<?> clazz, String database) {
        super(clazz, database);
    }

    public AntifraudBaseTest(Class<?> clazz, int port, String database) {
        super(clazz, port, database);
    }

    public CheckResult runtTestScenario(Runnable testScenario) {
        try {
            testScenario.run();
            return CheckResult.correct();
        } catch (UnexpectedResultException exception) {
            return exception.getResult();
        }
    }


    public void log(String message, Object... args) {
        final String param = String.format(message.replaceAll("\\{}", "%s"), args);
        System.out.println(param);
    }


    public Map<String, String> getAuthorizationHeader(User user) {
        String auth = user.getUsername() + ":" + user.getPassword();
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);

        Map<String, String> authParameter = new HashMap<>();
        authParameter.put("Authorization", authHeader);
        return authParameter;
    }

    public abstract User getDefaultAdmin();

    public Map<String, String> getDefaultAdminAuthorization() {
        return getAuthorizationHeader(getDefaultAdmin());
    }

    public HttpResponse sendRequestAndExpectResponse(String address, HttpRequestType type, String jsonPayloads, User user, int expectedCode) {
        HttpRequest httpRequest = getHttpRequest(address, type, jsonPayloads);
        if (user != null) {
            httpRequest.addHeaders(getAuthorizationHeader(user));
        }
        final HttpResponse send = httpRequest.send();
        if (send.getStatusCode() != expectedCode) {
            //TODO complete it
            String feedback = "";
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
        return send;
    }

    private HttpRequest getHttpRequest(String address, HttpRequestType type, String jsonPaylod) {
        HttpRequest httpRequest = null;
        switch (type) {
            case GET: {
                httpRequest = this.get(address);
                break;
            }
            case POST: {
                httpRequest = this.post(address, jsonPaylod);
                break;
            }
            case DELETE: {
                httpRequest = this.delete(address);
                break;
            }
            default:
                throw new RuntimeException("Type " + type + " is not supported");
        }
        return httpRequest;
    }
}
