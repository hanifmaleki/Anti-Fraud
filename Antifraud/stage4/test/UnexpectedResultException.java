import org.hyperskill.hstest.testcase.CheckResult;

class UnexpectedResultException extends RuntimeException {
    private final CheckResult result;

    UnexpectedResultException(CheckResult result) {
        this.result = result;
    }

    public CheckResult getResult() {
        return result;
    }
}
