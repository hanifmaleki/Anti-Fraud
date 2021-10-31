package hassured;

import java.util.function.BiFunction;
import java.util.function.Function;

public class HCompareMatcher<T> extends HMatcher<T> {

    private final T item;
    private final BiFunction<T, T, Boolean> comparator;
    private final Function<T, String> descriptor;

    protected HCompareMatcher(T item, BiFunction<T, T, Boolean> comparator, Function<T, String> descriptor) {
        this.item = item;
        this.comparator = comparator;
        this.descriptor = descriptor;
    }


    @Override
    public String describeMismatch(Object object) {
        String actual = object.toString();
        try {
            T generic = (T) object;
            actual = descriptor.apply(generic);
        } catch (ClassCastException e) {
        }
        return String.format("Expected %s but was %s", descriptor.apply(item), actual);
    }

    @Override
    public boolean test(T t) {
        return comparator.apply(t, item);
    }
}
