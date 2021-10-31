package hassured;

import exception.UnexpectedResultException;
import hassured.collection.Contains;
import hassured.collection.HasSize;
import org.hyperskill.hstest.testcase.CheckResult;

import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;


public class HMatchers {


    public static void assertThat(Object actual, HMatcher matcher) {
        if (!matcher.test(actual)) {
            String feedback = matcher.describeMismatch(actual);
            final CheckResult wrong = CheckResult.wrong(feedback);
            throw new UnexpectedResultException(wrong);
        }
    }

    public static <T> Contains contains(T item, BiPredicate<T, T> predicate) {
        final Function<T, String> function = (Function) (obj) -> "expect "+ obj.toString();
        return new Contains(item, predicate, function);
    }

    public static <T> Contains contains(T item) {
        final Function<T, String> function = (Function) (obj) -> "expect "+ obj.toString();
        BiPredicate predicate = (o1, o2) -> o1.equals(o2);
        return new Contains(item, predicate, function);
    }

    public static HasSize hasSize(int size) {
        return new HasSize(size);
    }

    public static <T> HCompareMatcher<T> is(T item) {

        Function<T, String> function = t -> "expect " + t.toString();
        BiFunction<T, T, Boolean> comparator = new BiFunction<T, T, Boolean>() {
            @Override
            public Boolean apply(T t1, T t2) {
                return t1.equals(t2);
            }
        };
        return new HCompareMatcher<T>(item, comparator, function);
    }

}
