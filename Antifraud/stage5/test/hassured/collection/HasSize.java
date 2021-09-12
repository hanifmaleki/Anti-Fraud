package hassured.collection;

import hassured.HMatcher;

import java.util.Collection;

public class HasSize extends HMatcher<Collection<?>> {
    private final int expectedSize;

    public HasSize(int expectedSize) {
//        super(item, comparator, descriptor);
        this.expectedSize = expectedSize;
    }

    @Override
    public boolean test(Collection<?> objects) {
        return objects.size() == expectedSize;
    }

    @Override
    public String describeMismatch(Object object) {
        return null;
    }
}
