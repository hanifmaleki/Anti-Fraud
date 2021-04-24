import antifraud.AntifraudApplication;
import antifraud.model.Role;
import antifraud.model.User;
import com.google.gson.Gson;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.List;


public class AntifraudTest extends SpringTest {

    private static final String address = "/api/antifraud/user";

    private final User user1;
    private final User user2;

    private final Gson gson = new Gson();

    public AntifraudTest() {
        super(AntifraudApplication.class);
        user1 = User
                .builder()
                .name("John Doe")
                .username("john_doe")
                .role(Role.USER)
                .build();

        user2 = User
                .builder()
                .name("Richard Roe")
                .username("richard_roe")
                .role(Role.ADMIN)
                .build();
    }


    @DynamicTest
    CheckResult testUser1() {
        // Add User1 and expect 200
        addUSerAndExceptSuccess(user1);

        // Get Users
        List<User> users = getUsersAndExpectSize(1);


        // Check The only element is user1
        User receivedUser = users.get(0);
        boolean isDifferent =
                !user1.getName().equals(receivedUser.getName()) ||
                        !user1.getUsername().equals(receivedUser.getUsername()) ||
                        user1.getRole() != receivedUser.getRole();

        if (isDifferent) {
            String feedback = String.format(
                    "The received user is not matched with created user.\n Get: %s \n Expected: %s",
                    receivedUser,
                    user1
            );
            return CheckResult.wrong(feedback);
        }


        // Add User1 and expect 200
        addUSerAndExceptSuccess(user2);


        // Get Users (User1, User2)
        users = getUsersAndExpectSize(2);
        //TODO check user

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
        return httpRequest.send();
    }

    private HttpResponse addUser(User user) {
        String user1Json = gson.toJson(user1);
        HttpRequest httpRequest = post(address, user1Json);
        return httpRequest.send();
    }


    private void addUSerAndExceptSuccess(User user) {
        // Add User1
        HttpResponse response = this.addUser(user);
        // Except code 200
        if (response.getStatusCode() != HttpStatus.OK.value()) {
            String feedback = String.format("POST %s should respond with status code 200, responded: %d\n\n" +
                            "Response body:\n%s"
                    , address, response.getStatusCode(), response.getContent());
            CheckResult result = CheckResult.wrong(feedback);

            throw new UnexpectedResultException(result);
        }
    }

    private List<User> getUsers() {
        return this.getUsersAndExpectSize(null);
    }

    private List<User> getUsersAndExpectSize(Integer expectedSize) {
        HttpRequest httpRequest = get(address);
        HttpResponse response = httpRequest.send();

        if (response.getStatusCode() != HttpStatus.OK.value()) {
            String feedback = String.format("GET %s should respond with status code 200, responded: %d\n\n" +
                            "Response body:\n%s"
                    , address, response.getStatusCode(), response.getContent());
            CheckResult result = CheckResult.wrong(feedback);
            throw new UnexpectedResultException(result);
        }

        List<User> users = Arrays.asList(gson.fromJson(response.getContent(), User[].class));

        if (expectedSize != null) {
            int size = users.size();
            if (size != 1) {
                String feedback = String.format("Number of users should be 1. The received list size is %d", size);
                CheckResult result = CheckResult.wrong(feedback);
                throw new UnexpectedResultException(result);
            }
        }

        return users;
    }

    class UnexpectedResultException extends RuntimeException {
        private final CheckResult result;

        UnexpectedResultException(CheckResult result) {
            this.result = result;
        }

        public CheckResult getResult() {
            return result;
        }
    }
}
