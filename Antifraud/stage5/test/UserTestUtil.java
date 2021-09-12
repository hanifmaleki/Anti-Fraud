import antifraud.model.User;
import exception.UnexpectedResultException;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.testcase.CheckResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import util.AntifraudBaseTest;
import util.MyBaseTestUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static data.UserDataProvider.USER_ADDRESS;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

public class UserTestUtil extends MyBaseTestUtil<User> {


    private final User defaultAdmin;

    public UserTestUtil(AntifraudBaseTest testClass) {
        super(testClass, User.class);
        defaultAdmin = testClass.getDefaultAdmin();

    }


    //TODO replace with assert
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
        this.remove(username, removerUser);
    }


    public void deleteNonExistingUser(User removerUser, String username) {
        final int notFound = NOT_FOUND.value();
        String feedback = String.format("Not existing user %s deletion should return code %d "
                , username, notFound);
        this.sendDeleteAndExpect(username, removerUser, notFound, feedback);
    }


    public void addUserAndExceptStatus(User adderUser, User addingUser, HttpStatus httpStatus) {
        this.sendPostRequestAndExpect(adderUser, addingUser, httpStatus.value(), null);
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
        final List<User> users = this.get(testClass.getDefaultAdmin());

        users.remove(testClass.getDefaultAdmin());

        //TODO Add HMatcher
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

    @Override
    public String getBaseAddress() {
        return USER_ADDRESS;
    }

    @Override
    protected String getEntityName() {
        return "User";
    }
}
