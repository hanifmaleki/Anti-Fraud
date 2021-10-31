package hassured.collection;

import hassured.HMatcher;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ContainsAllInAnyOrder<T> extends HMatcher<Collection<T>> {
    private final Collection<T> items;
    private final BiFunction<T, T, Boolean> comparator;
    private final Function<T, String> descriptor;

    private StringBuilder mismatch = new StringBuilder();

    public ContainsAllInAnyOrder(Collection<T> items, BiFunction<T, T, Boolean> comparator, Function<T, String> descriptor) {
        this.items = items;
        this.comparator = comparator;
        this.descriptor = descriptor;
    }

    @Override
    public boolean test(Collection<T> objects) {
        boolean match = true;
        for (T item : items) {
            if (!matchItem(objects, item)) {
                match = false;
                if (mismatch.length() > 0) {
                    mismatch.append(", ");
                } else {
                    mismatch.append("Could not find item(s) ");
                }
                mismatch.append(descriptor.apply(item));
            }
        }

        if (!match) {
            final String collect = objects
                    .stream()
                    .map(descriptor)
                    .collect(Collectors.joining(",", "[", "]"));
            mismatch.append(collect);
        }

        return match;
    }

    private boolean matchItem(Collection<T> objects, T item) {
        return objects.stream().anyMatch(object -> comparator.apply(item, object));
    }

    @Override
    public String describeMismatch(Object object) {
        return mismatch.toString();
    }
}
