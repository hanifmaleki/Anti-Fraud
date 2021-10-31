package hassured;

import java.util.function.Predicate;

public abstract class HMatcher<T>  implements Predicate<T> {

    public abstract String describeMismatch(Object object);


}
