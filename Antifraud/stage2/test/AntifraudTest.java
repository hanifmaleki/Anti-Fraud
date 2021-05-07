import antifraud.AntifraudApplication;
import antifraud.model.Role;
import antifraud.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.*;


public class AntifraudTest extends SpringTest {

    private static final String address = "/api/antifraud/user";

    private final User user1;
    private final User user2;
    private final User userWithoutRole;
    private final User userWithoutName;
    private final User userWithoutUsername;

    //    private final Gson gson = new Gson();
    private ObjectMapper objectMapper = new ObjectMapper();

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

        userWithoutRole = User
                .builder()
                .name("John Doe")
                .username("john_doe")
                .build();

        userWithoutName = User
                .builder()
                .username("john_doe")
                .role(Role.USER)
                .build();

        userWithoutUsername = User
                .builder()
                .username("john_doe")
                .role(Role.USER)
                .build();
    }


    @DynamicTest
        // Normal Behaviour
    CheckResult testUser1() {
        try {
            test1MainScenario();
            return CheckResult.correct();
        } catch (UnexpectedResultException exception) {
            return exception.getResult();
        }
    }

    @DynamicTest
        // Adding incomplete users and expect 209
    CheckResult testUser2() {
        try {
            test2MainScenario();
            return CheckResult.correct();
        } catch (UnexpectedResultException exception) {
            return exception.getResult();
        }
    }


    @DynamicTest
        // Delete non-existing user and except 404 NOT_FOUND
    CheckResult testUser3() {
        try {
            test3MainScenario();
            return CheckResult.correct();
        } catch (UnexpectedResultException exception) {
            return exception.getResult();
        }
    }

    private void test1MainScenario() {
        // Add User1 and expect 200
        addUserAndExceptStatus(user1, CREATED);

        // Get Users
        List<User> users = getUsersAndExpectSize(1);


        // Check The only element is user1
        checkUserExistingList(users, user1);

        // Add User2 and expect 200
        addUserAndExceptStatus(user2, CREATED);


        // Get Users (User1, User2)
        users = getUsersAndExpectSize(2);
        checkUserExistingList(users, user2);

        // Delete User1
        deleteExistingUser(user1.getUsername());

        // Get Users
        users = getUsersAndExpectSize(1);
        checkUserExistingList(users, user2);
        // Delete User2
        deleteExistingUser(user2.getUsername());

        // Get Empty List
        getUsersAndExpectSize(0);

    }

    private void test2MainScenario() {
        // Add User1_Incomplete1
        addUserAndExceptStatus(userWithoutName, UNPROCESSABLE_ENTITY);
        addUserAndExceptStatus(userWithoutRole, UNPROCESSABLE_ENTITY);
        addUserAndExceptStatus(userWithoutUsername, UNPROCESSABLE_ENTITY);


        // GET Empty List
        getUsersAndExpectSize(0);

        addUserAndExceptStatus(user1, CREATED);
        addUserAndExceptStatus(user1, CONFLICT);

        deleteExistingUser(user1.getUsername());

        // GET Empty List
        getUsersAndExpectSize(0);

    }

    private void test3MainScenario() {

        // Add Use1
        addUserAndExceptStatus(user1, CREATED);

        // Add User2
        addUserAndExceptStatus(user2, CREATED);

        // Delete Username3
        //Except Error_Code
        deleteNonExistingUser("username3");

    }


    private void checkUserExistingList(List<User> users, User user) {
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
    }

    private void deleteExistingUser(String username) {
        HttpRequest delete = delete(address + "/" + username);
        HttpResponse response = delete.send();
        if (response.getStatusCode() != OK.value()) {
            String feedback = String.format("Could not delete existing user %s", username);
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
    }

    private void deleteNonExistingUser(String username) {
        HttpRequest delete = delete(address + "/" + username);
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
        HttpRequest httpRequest = post(address, user1Json);
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
                    , address, status, response.getStatusCode(), response.getContent());
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

        if (response.getStatusCode() != OK.value()) {
            String feedback = String.format("GET %s should respond with status code 200, responded: %d\n\n" +
                            "Response body:\n%s"
                    , address, response.getStatusCode(), response.getContent());
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

    class UnexpectedResultException extends RuntimeException {
        private final CheckResult result;

        UnexpectedResultException(CheckResult result) {
            this.result = result;
        }

        public CheckResult getResult() {
            return result;
        }
    }

    public String toJson(Object onject) {
        try {
            return objectMapper.writeValueAsString(onject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T fromJson(HttpResponse response, Class<T> clazz) {
        try {
            T object = objectMapper.readValue(response.getContent(), clazz);
            return object;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
