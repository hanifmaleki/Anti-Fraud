import antifraud.AntifraudApplication;
import antifraud.model.ResultEnum;
import antifraud.model.Transaction;
import antifraud.model.TransactionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

public class AntifraudTest extends AntifraudBaseTest {

    private final TestDataProvider data = new TestDataProvider();
    private final UserTestUtil userUtil = new UserTestUtil(this);
    private final TransactionTestUtil transactionUtil = new TransactionTestUtil(this);
    private final CardAndIPTestUtil carIpUtil = new CardAndIPTestUtil(this);


    public AntifraudTest() {
        super(AntifraudApplication.class);

    }

    @DynamicTest
    // Test IP rest controller
    CheckResult test1() {
        return runtTestScenario(null);

    }

    @DynamicTest
    // Adding incomplete users and expect 209
    CheckResult test2() {
        return runtTestScenario(null);
    }


    @DynamicTest
    // Delete non-existing user and except 404 NOT_FOUND
    CheckResult test3() {
        return runtTestScenario(null);
    }


}
