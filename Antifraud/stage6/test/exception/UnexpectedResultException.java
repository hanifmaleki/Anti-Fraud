package exception;
import org.hyperskill.hstest.testcase.CheckResult;

public class UnexpectedResultException extends RuntimeException {
    private final CheckResult result;

    public UnexpectedResultException(CheckResult result) {
        this.result = result;
    }

    public CheckResult getResult() {
        return result;
    }
}
