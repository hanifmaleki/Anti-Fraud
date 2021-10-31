package hassured.http;

import hassured.HMatcher;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;

public class HasResponseCode extends HMatcher<HttpResponse> {

    private final int expectedCode;

    public HasResponseCode(int expectedCode) {
//        super(item, comparator, descriptor);
        this.expectedCode = expectedCode;
    }

/*    public static HasResponseCode hasResponseCode(int code) {
        return new HasResponseCode(code);
    }*/

    @Override
    public boolean test(HttpResponse httpResponse) {
        return httpResponse.getStatusCode() == expectedCode;
    }

    @Override
    public String describeMismatch(Object object) {
        return null;
    }
}
