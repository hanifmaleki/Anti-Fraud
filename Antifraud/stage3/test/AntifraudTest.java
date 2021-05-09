import antifraud.AntifraudApplication;
import antifraud.model.ResultEnum;
import antifraud.model.Transaction;
import antifraud.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private static final String BASE_ADDRESS = "/api/antifraud";
    private static final String IP_ADDRESS = BASE_ADDRESS + "/stolencard";
    private static final String STOLEN_ADDRESS = BASE_ADDRESS + "/suspicious-ip";
    private static final String TRX_ADDRESS = BASE_ADDRESS + "/transaction";

    private final String stolenCard1;
    private final String stolenCard2;
    private final String okCard;
    private final String suspiciousIp1;
    private final String suspiciousIp2;
    private final String okIp;
    private final Transaction trxAllowed;
    private final Transaction trxManuall;
    private final Transaction trxProhibited1;
    private final Transaction trxProhibited2;
    private final Transaction trxProhibited3;


    private ObjectMapper objectMapper = new ObjectMapper();

    public AntifraudTest() {
        super(AntifraudApplication.class);
        stolenCard1 = "2223003122003222";
        stolenCard2 = "5200828282828210";
        okCard = "5105105105105100";
        suspiciousIp1 = "192.168.0.12";
        suspiciousIp2 = "198.18.0.6";
        okIp = "172.16.0.9";

        trxAllowed = Transaction
                .builder()
                .amount(60)
                .ipAddress(okIp)
                .cardSerial(okCard)
                .build();

        trxManuall = Transaction
                .builder()
                .amount(400)
                .ipAddress(okIp)
                .cardSerial(okCard)
                .build();

        trxProhibited1 = Transaction
                .builder()
                .amount(60)
                .ipAddress(okIp)
                .cardSerial(stolenCard2)
                .build();

        trxProhibited2 = Transaction
                .builder()
                .amount(400)
                .ipAddress(suspiciousIp2)
                .cardSerial(okCard)
                .build();

        trxProhibited3 = Transaction
                .builder()
                .amount(2001)
                .ipAddress(suspiciousIp1)
                .cardSerial(stolenCard1)
                .build();
    }


    @DynamicTest
        // Test IP rest controller
    CheckResult testSuspiciousIp1() {
        return runtTestScenario(this::test1MainScenario);
    }

    @DynamicTest
        // Adding incomplete users and expect 209
    CheckResult testUser2() {
        return runtTestScenario(this::test2MainScenario);
    }


    @DynamicTest
        // Delete non-existing user and except 404 NOT_FOUND
    CheckResult testUser3() {
        return runtTestScenario(this::test3MainScenario);
    }


    private void test1MainScenario() {
        // Add stolen card1
        // Get cards (1)
        // Check it is card1

        // Add card1 another time and expect 409

        // Add stolen card2
        // Get cards (2)
        // Delete stolen card1
        // Get cards (1)
        // Check cards2
        // Delete card(2)
        // Get empty card list
        // Delete non-existing card except and expect 404
    }

    private void test2MainScenario() {
        // Add suspicious ip-1
        // Get list (1)
        // Check it is ip-1

        // Add IP-1 another time and expect 409

        // Add suspicious ip2
        // Get list (2)
        // Delete IP1
        // Get cards (1)
        // Check IP2
        // Delete IP2
        // Get empty list
        // Delete non-existing IP and except and expect 404
    }

    private void test3MainScenario() {
        // Add stolen-card-1
        // Add stolen-card-2
        // Add suspicious-IP-1
        // Add suspicious-IP-2

        // test an allowed transaction

        // test a manual-processing transaction

        // test prohibitedTrx-1
        // except card reason

        // test prohibitedTrx-2
        // except ip reason

        // Given trxProhibited3 = amount > 2000 && card = card-1 && IP = ip1
        // test trxX and except prohibited + all messages

        //delete card-1
        // test trxProhibited3 and except prohibited + amount messages + ip message

        //delete IP-1
        // test trxProhibited3 and except prohibited + amount messages

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

    private void addStolenCardAndExpectStatus(String serialNumber, HttpStatus expectedStatus) {
        String json = toJson(serialNumber);
        HttpRequest postRequest = post(STOLEN_ADDRESS, json);
        HttpResponse send = postRequest.send();
        int expectedCode = expectedStatus.value();
        int receivedCode = send.getStatusCode();
        if (receivedCode != expectedCode) {
            String feedback = String.format("Expected status %d after adding card %s but received status %d",
                    expectedCode, serialNumber, receivedCode);
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
    }

    private void getStolenCardsAndExpectSize(int expectedSize) {
    }

    private void deleteCardAndExpect(String serialNumber, HttpStatus expectedStatus) {
    }

    private void checkItemExistInList(String item, List<String> list) {
    }

    private void addSuspiciousIpAndExpectStatus(String ip, HttpStatus expectedStatus) {
    }

    private void getSuspiciousIpsAndExpectSize(int expectedSize) {
    }

    private void deleteSuspiciousIpAndExpect(String ip, HttpStatus expectedStatus) {
    }

    private void queryTrxAndExpect(Transaction transaction, ResultEnum expectedResult, String... expectedMessageWords) {
    }

    private void deleteExistingUser(String username) {
        HttpRequest delete = delete(BASE_ADDRESS + "/" + username);
        HttpResponse response = delete.send();
        if (response.getStatusCode() != OK.value()) {
            String feedback = String.format("Could not delete existing user %s", username);
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
    }

    private void deleteNonExistingUser(String username) {
        HttpRequest delete = delete(BASE_ADDRESS + "/" + username);
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
        HttpRequest httpRequest = post(BASE_ADDRESS, user1Json);
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
                    , BASE_ADDRESS, status, response.getStatusCode(), response.getContent());
            CheckResult result = CheckResult.wrong(feedback);

            throw new UnexpectedResultException(result);
        }
    }

    private List<User> getUsers() {
        return this.getUsersAndExpectSize(null);
    }

    private List<User> getUsersAndExpectSize(Integer expectedSize) {
        HttpRequest httpRequest = get(BASE_ADDRESS);
        HttpResponse response = httpRequest.send();

        if (response.getStatusCode() != OK.value()) {
            String feedback = String.format("GET %s should respond with status code 200, responded: %d\n\n" +
                            "Response body:\n%s"
                    , BASE_ADDRESS, response.getStatusCode(), response.getContent());
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


    private CheckResult runtTestScenario(Runnable testScenario) {
        try {
            testScenario.run();
            return CheckResult.correct();
        } catch (UnexpectedResultException exception) {
            return exception.getResult();
        }
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
