import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;

public class AntifraudBaseTest extends SpringTest {
    public AntifraudBaseTest(Class<?> clazz) {
        super(clazz);
    }

    public AntifraudBaseTest(Class<?> clazz, int port) {
        super(clazz, port);
    }

    public AntifraudBaseTest(Class<?> clazz, String database) {
        super(clazz, database);
    }

    public AntifraudBaseTest(Class<?> clazz, int port, String database) {
        super(clazz, port, database);
    }

    public CheckResult runtTestScenario(Runnable testScenario) {
        try {
            testScenario.run();
            return CheckResult.correct();
        } catch (UnexpectedResultException exception) {
            return exception.getResult();
        }
    }


    public void log(String message, Object... args) {
        String param = message;
        for (Object arg : args) {
            param = param.replaceFirst("\\{}", arg.toString());
        }
        System.out.println(param);
    }
}
