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
    CheckResult testUser1() {
        // Add User1
            // Given user1
            // Add it
            // Except code 200
        // Get User1
            // Get User
            // Except a list of 1
            // Check the element is user1
        // Add User2
            // Given user1
            // Add it
            // Except code 200
        // Get User1, User2
        // Delete User1
        // Get User2
        // Delete User2
        // Get EMpty List
        return null;
    }

    @DynamicTest
    CheckResult testUser2() {
        // Add User1_Incomplete1
            //Except Error-Code
        // Add User2_Incomplete2
            //Except Error_Code
        // GET Empty List
        // Add Use1
        // Add User2
        // Delete Username3
            //Except Error_Code

        return null;

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
