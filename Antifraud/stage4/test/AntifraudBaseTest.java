import antifraud.model.User;
import org.assertj.swing.junit.dependency.commons_codec.binary.Base64;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;

import java.nio.charset.Charset;
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
}
