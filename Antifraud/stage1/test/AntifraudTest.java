import antifraud.AntifraudApplication;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;


public class AntifraudTest extends SpringTest {
    private static final String DOUBLE_CHECK_MESSAGE = "Double-check conditions";
    public static final String BOUNDARY_MESSAGE = "Take extra care to the boundary conditions";
    private static final String address = "/api/antifraud/transaction";

    public AntifraudTest() {
        super(AntifraudApplication.class);
    }

    @DynamicTest
    CheckResult testGet1() {
        long amount = 150;
        String expectedResult = "allowed";
        return callMethodAndCheckResult(amount, expectedResult, DOUBLE_CHECK_MESSAGE);
    }

    @DynamicTest
    CheckResult testGet2() {
        long amount = 200;
        String expectedResult = "allowed";
        return callMethodAndCheckResult(amount, expectedResult, BOUNDARY_MESSAGE);
    }

    @DynamicTest
    CheckResult testGet3() {
        long amount = 201;
        String expectedResult = "manual_processing";
        return callMethodAndCheckResult(amount, expectedResult, DOUBLE_CHECK_MESSAGE);
    }


    @DynamicTest
    CheckResult testGet4() {
        long amount = 1500;
        String expectedResult = "manual_processing";
        return callMethodAndCheckResult(amount, expectedResult, BOUNDARY_MESSAGE);
    }

    @DynamicTest
    CheckResult testGet5() {
        long amount = 1501;
        String expectedResult = "prohibited";
        return callMethodAndCheckResult(amount, expectedResult, DOUBLE_CHECK_MESSAGE);
    }

    @DynamicTest
    CheckResult testGet6() {
        long amount = 12345678987654321L;
        String expectedResult = "prohibited";
        return callMethodAndCheckResult(amount, expectedResult, DOUBLE_CHECK_MESSAGE);
    }

    private CheckResult callMethodAndCheckResult(@Nullable Long amount, String expectedResult, Object message) {

        HttpResponse response = getHttpResponse(amount);


        if (response.getStatusCode() != HttpStatus.OK.value()) {
            String feedback = String.format("GET %s should respond with status code 200, responded: %d\n\n" +
                            "Response body:\n%s"
                    , address, response.getStatusCode(), response.getContent());
            return CheckResult.wrong(feedback);
        }

        String result = response.getContent().toLowerCase().trim();
        if (!result.contains(expectedResult)) {
            String feedback = String.format("The expected result is %s, your result is %s. %s", expectedResult, result, message);
            return CheckResult.wrong(feedback);
        }

        return CheckResult.correct();
    }

    private HttpResponse getHttpResponse(Long amount) {
        HttpRequest httpRequest = get(address);
        if (amount != null) {
            String parameter = String.valueOf(amount);
            httpRequest.addParam("amount", parameter);
        }
        HttpResponse response = httpRequest.send();
        return response;
    }
}
