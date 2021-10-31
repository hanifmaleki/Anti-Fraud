package hassured.collection;

import hassured.HMatcher;

import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Contains<T> extends HMatcher<Collection<T>> {
    private final T item;
    private final BiPredicate<T, T> predicate;
    private final Function<T, String> descriptor;
    private String collectionString = "";

    private StringBuilder mismatch = new StringBuilder();

    public Contains(T item, BiPredicate<T, T> predicate, Function<T, String> descriptor) {
        this.item = item;
        this.predicate = predicate;
        this.descriptor = descriptor;
    }

    @Override
    public boolean test(Collection<T> objects) {
        final boolean match = objects.stream()
                .anyMatch(item -> predicate.test(item, this.item));

        if (!match) {
            this.collectionString = objects
                    .stream()
                    .map(descriptor)
                    .collect(Collectors.joining(",", "[", "]"));
        }

        return match;
    }

    @Override
    public String describeMismatch(Object object) {
        String desc = object.toString();
        try {
            T item = (T) object;
            desc = descriptor.apply(item);
        } catch (Exception e) {
        }

        mismatch.append("could not find ")
                .append(desc)
                .append(" in ")
                .append(collectionString);

        return mismatch.toString();
    }
}
