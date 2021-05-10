import antifraud.AntifraudApplication;
import antifraud.model.ResultEnum;
import antifraud.model.Transaction;
import antifraud.model.TransactionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.ContentType;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

public class AntifraudTest extends SpringTest {

    private static final String BASE_ADDRESS = "/api/antifraud";
    private static final String IP_ADDRESS = BASE_ADDRESS + "/suspicious-ip";
    private static final String STOLEN_ADDRESS = BASE_ADDRESS + "/stolencard";
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
        stolenCard1 = "2223-0031-2200-3222";
        stolenCard2 = "5200-8282-8282-8210";
        okCard = "5105-1051-0510-5100";
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
    CheckResult test1SuspiciousIp() {
        return runtTestScenario(this::testIpScenario);

    }

    @DynamicTest
        // Adding incomplete users and expect 209
    CheckResult test2Card() {
        return runtTestScenario(this::testCardScenario);
    }


    @DynamicTest
        // Delete non-existing user and except 404 NOT_FOUND
    CheckResult test3Transaction() {
        return runtTestScenario(this::testTransactionScenario);
    }


    private void testCardScenario() {
        // Add stolen card1
        log("Add stolen card {}", stolenCard1);
        addStolenCardAndExpectStatus(stolenCard1, CREATED);

        // Get cards (1)
        log("Get card list and expect size 1");
        List<String> stolenCards = getStolenCardsAndExpectSize(1);
        // Check it is card1
        log("Check card {} exist in list {}", stolenCard1, stolenCards);
        checkItemExistInList(stolenCards, stolenCard1);

        // Add card1 another time and expect 409
        log("Add existing stolen card {} and except conflict", stolenCard1);
        addStolenCardAndExpectStatus(stolenCard1, CONFLICT);

        // Add stolen card2
        log("Add stolen card {}", stolenCard2);
        addStolenCardAndExpectStatus(stolenCard2, CREATED);
        // Get cards (2)
        log("Get card list and expect size 2");
        getStolenCardsAndExpectSize(2);

        // Delete stolen card1
        log("Delete stolen card {}", stolenCard1);
        deleteCardAndExpect(stolenCard1, OK);
        // Get cards (1)
        log("Get card list and expect size 1");
        stolenCards = getStolenCardsAndExpectSize(1);
        // Check cards2
        log("Check card {} exist in list {}", stolenCard2, stolenCards);
        checkItemExistInList(stolenCards, stolenCard2);

        // Delete non-existing card except and expect 404
        log("Delete non-existing stolen card {} and except NOT_FOUND", stolenCard1);
        deleteCardAndExpect(stolenCard1, NOT_FOUND);

        // Delete card(2)
        log("Delete stolen card {}", stolenCard2);
        deleteCardAndExpect(stolenCard2, OK);

        // Get empty card list
        log("Get empty stolen card list");
        getStolenCardsAndExpectSize(0);

    }

    private void testIpScenario() {
        // Add suspicious ip-1
        log("Add suspicious IP {}", suspiciousIp1);
        addSuspiciousIpAndExpectStatus(suspiciousIp1, CREATED);

        // Get list (1)
        log("Get IP list and expect size 1");
        List<String> suspiciousIps = getSuspiciousIpsAndExpectSize(1);
        // Check it is ip-1
        log("Check {} exist in list {}", suspiciousIp1, suspiciousIps);
        checkItemExistInList(suspiciousIps, suspiciousIp1);

        // Add IP-1 another time and expect 409
        log("Add already existing suspicious IP {} and expect conflict", suspiciousIp1);
        addSuspiciousIpAndExpectStatus(suspiciousIp1, CONFLICT);

        // Add suspicious ip2
        log("Add suspicious IP {}", suspiciousIp2);
        addSuspiciousIpAndExpectStatus(suspiciousIp2, CREATED);
        // Get list (2)
        log("Get IP list and expect size 2");
        getSuspiciousIpsAndExpectSize(2);

        // Delete IP1
        log("Delete IP {}", suspiciousIp1);
        deleteSuspiciousIpAndExpect(suspiciousIp1, OK);
        // Get cards (1)
        log("Get IP list and expect size 1");
        suspiciousIps = getSuspiciousIpsAndExpectSize(1);

        // Delete non-existing IP1 and except and expect 404
        log("Delete non-existing IP {} and except and expect 404", suspiciousIp1);
        deleteSuspiciousIpAndExpect(suspiciousIp1, NOT_FOUND);

        // Check IP2
        log("Check {} exist in list {}", suspiciousIp2, suspiciousIps);
        checkItemExistInList(suspiciousIps, suspiciousIp2);
        // Delete IP2
        log("Delete IP {}", suspiciousIp2);
        deleteSuspiciousIpAndExpect(suspiciousIp2, OK);

        // Get empty list
        log("Get empty Suspicious IP list");
        getSuspiciousIpsAndExpectSize(0);

    }

    private void testTransactionScenario() {
        // Add stolen-card-1
        log("Add stolen card {}", stolenCard1);
        addStolenCardAndExpectStatus(stolenCard1, CREATED);
        // Add stolen-card-2
        log("Add stolen card {}", stolenCard2);
        addStolenCardAndExpectStatus(stolenCard2, CREATED);

        // Add suspicious-IP-1
        log("Add suspicious IP {}", suspiciousIp1);
        addSuspiciousIpAndExpectStatus(suspiciousIp1, CREATED);
        // Add suspicious-IP-2
        log("Add suspicious IP {}", suspiciousIp2);
        addSuspiciousIpAndExpectStatus(suspiciousIp2, CREATED);

        // test an allowed transaction
        log("Expected allow result from transaction \n {}", trxAllowed);
        queryTrxAndExpect(trxAllowed, ResultEnum.ALLOWED);

        // test a manual-processing transaction
        log("Expected MANUAL result from transaction \n {}", trxManuall);
        queryTrxAndExpect(trxManuall, ResultEnum.MANUAL_PROCESSING, "confirmed manually");

        // test prohibitedTrx-1 and except card reason
        log("Expected CARD PROHIBITED result from transaction \n {}", trxProhibited1);
        queryTrxAndExpect(trxProhibited1, ResultEnum.PROHIBITED, "blacklist");

        // test prohibitedTrx-2 and except ip reason
        log("Expected IP PROHIBITED result from transaction \n {}", trxProhibited1);
        queryTrxAndExpect(trxProhibited2, ResultEnum.PROHIBITED, "suspicious");

        // Given trxProhibited3 = amount > 2000 && card = card-1 && IP = ip1
        // and except prohibited + all messages
        log("Expected all types PROHIBITED result from transaction \n {}", trxProhibited3);
        queryTrxAndExpect(trxProhibited3, ResultEnum.PROHIBITED, "above allowed value", "blacklist", "suspicious");

        //delete card-1
        log("Delete card {}", stolenCard1);
        deleteCardAndExpect(stolenCard1, OK);
        // test trxProhibited3 and except prohibited + amount messages + ip message
        log("Expected IP+VALUE PROHIBITED result from transaction \n {}", trxProhibited3);
        queryTrxAndExpect(trxProhibited3, ResultEnum.PROHIBITED, "above allowed value", "suspicious");

        //delete IP-1
        log("Delete suspicious IP {}", suspiciousIp1);
        deleteSuspiciousIpAndExpect(suspiciousIp1, OK);
        // test trxProhibited3 and except prohibited + amount messages
        log("Expected VALUE PROHIBITED result from transaction \n {}", trxProhibited3);
        queryTrxAndExpect(trxProhibited3, ResultEnum.PROHIBITED, "above allowed value");

    }


    private void addStolenCardAndExpectStatus(String serialNumber, HttpStatus expectedStatus) {
        Map<String, String> params = new HashMap<>();
        params.put("serialNumber", serialNumber);
        HttpRequest postRequest = post(STOLEN_ADDRESS, params);
        sendPostRequestAndExpect(serialNumber, expectedStatus, postRequest);
    }


    private List<String> getStolenCardsAndExpectSize(int expectedSize) {
        HttpRequest getRequest = get(STOLEN_ADDRESS);
        return getListOfStringWithExpectedSize(expectedSize, getRequest);
    }

    private void deleteCardAndExpect(String serialNumber, HttpStatus expectedStatus) {
        HttpRequest deleteRequest = delete(STOLEN_ADDRESS + "/" + serialNumber);
        sendDeleteRequestAndExpect(serialNumber, expectedStatus, deleteRequest);

    }

    private void addSuspiciousIpAndExpectStatus(String ip, HttpStatus expectedStatus) {
        Map<String, String> params = new HashMap<>();
        params.put("ip", ip);
        HttpRequest postRequest = post(IP_ADDRESS, params);
        sendPostRequestAndExpect(ip, expectedStatus, postRequest);
    }

    private List<String> getSuspiciousIpsAndExpectSize(int expectedSize) {
        HttpRequest getRequest = get(IP_ADDRESS);
        return getListOfStringWithExpectedSize(expectedSize, getRequest);
    }

    private void deleteSuspiciousIpAndExpect(String ip, HttpStatus expectedStatus) {
        HttpRequest deleteRequest = delete(IP_ADDRESS + "/" + ip);
        System.out.println(deleteRequest.getLocalUri());
        sendDeleteRequestAndExpect(ip, expectedStatus, deleteRequest);
    }

    private void queryTrxAndExpect(Transaction transaction, ResultEnum expectedResult, String... expectedMessageWords) {
        final String trxJson = toJson(transaction);
        HttpRequest getRequest = post(TRX_ADDRESS, trxJson);
        HttpResponse response = getRequest.send();
        TransactionResponse transactionResponse = fromJson(response, TransactionResponse.class);

        ResultEnum receivedResult = transactionResponse.getResult();
        if (receivedResult != expectedResult) {
            String feedback = String.format("The transaction %s result is %s but expected to be %s",
                    transaction.toString(), receivedResult, expectedResult);
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }

        String message = transactionResponse.getMessage();
        List<String> missingWords = Arrays.stream(expectedMessageWords)
                .map(String::toLowerCase)
                .filter(word -> !message.contains(word.toLowerCase()))
                .collect(Collectors.toList());
        if (!missingWords.isEmpty()) {
            String feedback = missingWords.stream()
                    .collect(Collectors.joining("\n", "The result message is missing the following phrase(s):\n", ""));
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
    }

    private List<String> getListOfStringWithExpectedSize(int expectedSize, HttpRequest getRequest) {
        HttpResponse httpResponse = getRequest.send();
        int receivedCode = httpResponse.getStatusCode();
        if (receivedCode != OK.value()) {
            String feedback = String.format("Unexpected get result %d", receivedCode);
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
        List<String> list = Arrays.asList(fromJson(httpResponse, String[].class));
        int receivedSize = list.size();
        if (receivedSize != expectedSize) {
            String feedback = String.format("The expected list size is %d, however received %d", expectedSize, receivedSize);
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
        return list;
    }

    private void sendPostRequestAndExpect(String serialNumber, HttpStatus expectedStatus, HttpRequest postRequest) {
        HttpResponse httpResponse = postRequest.send();
        int expectedCode = expectedStatus.value();
        int receivedCode = httpResponse.getStatusCode();
        if (receivedCode != expectedCode) {
            String feedback = String.format("Expected status %d after adding item %s but received status %d for request:\n%s",
                    expectedCode, serialNumber, receivedCode, postRequest.getUri());
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
    }

    private void sendDeleteRequestAndExpect(String serialNumber, HttpStatus expectedStatus, HttpRequest deleteRequest) {
        HttpResponse response = deleteRequest.send();

        int statusCode = response.getStatusCode();
        int expectedValue = expectedStatus.value();
        if (statusCode != expectedValue) {
            String feedback = String.format("Delete operation of %s expect code %d but received code %d"
                    , serialNumber, expectedValue, statusCode);
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
    }

    private void checkItemExistInList(List<String> list, String item) {
        if (!list.contains(item)) {
            String feedback = String.format("Can not find item %s in the list", item);
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
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
            return objectMapper.readValue(response.getContent(), clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void log(String message, Object... args) {
        String param = message;
        for (Object arg : args) {
            param = param.replaceFirst("\\{}", arg.toString());
        }
        System.out.println(param);
    }
}
