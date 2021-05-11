import antifraud.model.User;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

public class UserTestUtil extends BaseTestUtil {
    public UserTestUtil(SpringTest testClass) {
        super(testClass);
    }


      /* private void checkUserExistingList(List<User> users, User user) {
        User receivedUser = users.stream().filter(user::equals).findFirst().orElseThrow(() -> {
            String feedback = String.format("The expected user %s does nt exist in the received list", user1);
            return new UnexpectedResultException(CheckResult.wrong(feedback));
        });

        boolean isDifferent =
                !user.getName().equals(receivedUser.getName()) ||
                        !user.getUsername().equals(receivedUser.getUsername()) ||
                        user.getRole() != receivedUser.getRole();

        if (isDifferent) {
            String feedback = String.format(
                    "The received user is not matched with created user.\n Get: %s \n Expected: %s",
                    receivedUser,
                    user
            );
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
    }*/

    private void deleteExistingUser(String username) {
        HttpRequest delete = testClass.delete(TestDataProvider.USER_ADDRESS + "/" + username);
        HttpResponse response = delete.send();
        if (response.getStatusCode() != OK.value()) {
            String feedback = String.format("Could not delete existing user %s", username);
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
    }

    private void deleteNonExistingUser(String username) {
        HttpRequest delete = testClass.delete(TestDataProvider.USER_ADDRESS + "/" + username);
        HttpResponse response = delete.send();
        int statusCode = response.getStatusCode();
        int expectedValue = NOT_FOUND.value();
        if (statusCode != expectedValue) {
            String feedback = String.format("Not existing user %s deletion should return code %d and not %d"
                    , username, expectedValue, statusCode);
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
    }

    private HttpResponse addUser(User user) {
//        String user1Json = gson.toJson(user1);
        String user1Json = toJson(user);
        HttpRequest httpRequest = testClass.post(TestDataProvider.USER_ADDRESS, user1Json);
        return httpRequest.send();
    }

    private void addUserAndExceptStatus(User user, HttpStatus httpStatus) {
        // Add User
        HttpResponse response = this.addUser(user);
        // Except httpStatus
        int status = httpStatus.value();
        if (response.getStatusCode() != status) {
            String feedback = String.format("POST %s should respond with status code %d, responded: %d\n\n" +
                            "Response body:\n%s"
                    , TestDataProvider.USER_ADDRESS, status, response.getStatusCode(), response.getContent());
            CheckResult result = CheckResult.wrong(feedback);

            throw new UnexpectedResultException(result);
        }
    }

    private List<User> getUsers() {
        return this.getUsersAndExpectSize(null);
    }

    private List<User> getUsersAndExpectSize(Integer expectedSize) {
        HttpRequest httpRequest = testClass.get(TestDataProvider.USER_ADDRESS);
        HttpResponse response = httpRequest.send();

        if (response.getStatusCode() != OK.value()) {
            String feedback = String.format("GET %s should respond with status code 200, responded: %d\n\n" +
                            "Response body:\n%s"
                    , TestDataProvider.USER_ADDRESS, response.getStatusCode(), response.getContent());
            CheckResult result = CheckResult.wrong(feedback);
            throw new UnexpectedResultException(result);
        }

//        List<User> users = Arrays.asList(gson.fromJson(response.getContent(), User[].class));
        List<User> users = Arrays.asList(fromJson(response, User[].class));

        if (expectedSize != null) {
            int size = users.size();
            if (size != expectedSize) {
                String feedback = String.format("Number of users should be 1. The received list size is %d", size);
                CheckResult result = CheckResult.wrong(feedback);
                throw new UnexpectedResultException(result);
            }
        }

        return users;
    }


}
