import antifraud.model.User;
import data.TestDataProvider;
import exception.UnexpectedResultException;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.testcase.CheckResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static data.UserDataProvider.USER_ADDRESS;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

public class UserTestUtil extends BaseTestUtil {


    private final User defaultAdmin;

    public UserTestUtil(AntifraudBaseTest testClass) {
        super(testClass);
        defaultAdmin = testClass.getDefaultAdmin();

    }

    public void checkUserExistIngList(List<User> users, User user) {
        User receivedUser = users.stream().filter(user::equals).findFirst().orElseThrow(() -> {
            String feedback = String.format("The expected user %s does nt exist in the received list", user);
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
    }


    public void deleteExistingUser(User removerUser, String username) {
        final Map<String, String> authorizationParameter = testClass.getAuthorizationHeader(removerUser);
        HttpRequest delete = testClass.delete(USER_ADDRESS + "/" + username)
                .addHeaders(authorizationParameter);
        HttpResponse response = delete.send();
        if (response.getStatusCode() != OK.value()) {
            String feedback = String.format("Could not delete existing user %s", username);
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
    }

    public void deleteNonExistingUser(User removerUser, String username) {
        final Map<String, String> authorizationParameter = testClass.getAuthorizationHeader(removerUser);
        HttpRequest delete = testClass.delete(USER_ADDRESS + "/" + username);
        delete.addHeaders(authorizationParameter);
        HttpResponse response = delete.send();
        int statusCode = response.getStatusCode();
        int expectedValue = NOT_FOUND.value();
        if (statusCode != expectedValue) {
            String feedback = String.format("Not existing user %s deletion should return code %d and not %d"
                    , username, expectedValue, statusCode);
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
    }

    /**
     * Add user and except status
     *
     * @param adderUser  The user who want to add a new user
     * @param addingUser The new user to be added
     * @param httpStatus expected HTTP status
     * @throws UnexpectedResultException when the received status is different from expected
     */
    public void addUserAndExceptStatus(User adderUser, User addingUser, HttpStatus httpStatus) {
        // Add User
        String user1Json = toJson(addingUser);
        HttpRequest httpRequest = testClass.post(USER_ADDRESS, user1Json)
                .addHeaders(testClass.getAuthorizationHeader(adderUser));
        final HttpResponse response = httpRequest.send();
        // Except httpStatus
        int status = httpStatus.value();
        if (response.getStatusCode() != status) {
            String feedback = String.format("POST %s should respond with status code %d, responded: %d\n\n" +
                            "Response body:\n%s"
                    , USER_ADDRESS, status, response.getStatusCode(), response.getContent());
            CheckResult result = CheckResult.wrong(feedback);

            throw new UnexpectedResultException(result);
        }
    }

    public boolean isAuthorizedPasswordChange(User changerUser, User changingUser) {

        String address = String.format("%s/%s", USER_ADDRESS, changingUser.getUsername());
        final Map<String, String> authorizationParameter = testClass.getAuthorizationHeader(changerUser);
        // It does not change password, just check if it is possible
        final HttpRequest httpRequest = testClass.put(address, changingUser.getPassword())
                .addHeaders(authorizationParameter);
        final HttpResponse response = httpRequest.send();
        final int statusCode = response.getStatusCode();
        if (statusCode == OK.value()) {
            return true;
        }

        if (statusCode == HttpStatus.FORBIDDEN.value()) {
            return false;
        }

        String feedback = String.format("Could not change the password. The receiving HTTP code is %d", statusCode);
        throw new UnexpectedResultException(CheckResult.wrong(feedback));
    }

    public boolean isAuthorizedChangeOwnPassword(User user) {
        return isAuthorizedPasswordChange(user, user);
    }

    /**
     * Get list of users and except size
     *
     * @param expectedSize expected size
     * @return Number of users EXCLUDING the default user
     */
    public List<User> getUsersAndExpectSize(Integer expectedSize) {
        HttpRequest httpRequest = testClass.get(USER_ADDRESS)
                .addHeaders(testClass.getDefaultAdminAuthorization());
        HttpResponse response = httpRequest.send();

        if (response.getStatusCode() != OK.value()) {
            String feedback = String.format("GET %s should respond with status code 200, responded: %d\n\n" +
                            "Response body:\n%s"
                    , USER_ADDRESS, response.getStatusCode(), response.getContent());
            CheckResult result = CheckResult.wrong(feedback);
            throw new UnexpectedResultException(result);
        }

        List<User> users = Arrays.stream(fromJson(response, User[].class))
                .filter(item -> !item.equals(testClass.getDefaultAdmin()))
                .collect(Collectors.toList());

        users.remove(testClass.getDefaultAdmin());

        int size = users.size();
        if (size != expectedSize) {
            String feedback = String.format("Number of users should be 1. The received list size is %d", size);
            CheckResult result = CheckResult.wrong(feedback);
            throw new UnexpectedResultException(result);
        }


        return users;
    }

    public void checkHashingPassword(User user) {
        testClass.log("Adding user {} and expect hashed password", user);
        this.addUserAndExceptStatus(defaultAdmin, user, HttpStatus.CREATED);
        //Get users
        final List<User> users = this.getUsersAndExpectSize(1);
        //Check Hashed Password
        User receivedUSer = users.get(0);
        final String receivedPassword = receivedUSer.getPassword();
        final String userPlainTextPassword = user.getPassword();
        if (!BCrypt.checkpw(userPlainTextPassword, receivedPassword)) {
            String feedback = "The received hashed password is not matched with the given password";
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
        //Remove user-1
        this.deleteExistingUser(defaultAdmin, user.getUsername());
        //get empty user list
        this.getUsersAndExpectSize(0);
        testClass.log("Received password is equal to the expected one");
    }

}
