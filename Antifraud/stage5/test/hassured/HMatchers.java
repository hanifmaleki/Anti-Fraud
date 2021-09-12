package hassured;

import exception.UnexpectedResultException;
import org.hyperskill.hstest.testcase.CheckResult;


public class HMatchers {



    public static void assertThat(Object actual, HMatcher matcher){
        if(!matcher.test(actual)){
            String feedback = matcher.describeMismatch(actual);
            final CheckResult wrong = CheckResult.wrong(feedback);
            throw new UnexpectedResultException(wrong);
        }
    }
}
