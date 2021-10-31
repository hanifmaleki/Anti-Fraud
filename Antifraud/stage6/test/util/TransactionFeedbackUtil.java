package util;

import antifraud.model.ResultEnum;
import antifraud.model.User;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;

import java.util.function.Function;

public class TransactionFeedbackUtil extends BaseRestTestUtil<Long>{


    public TransactionFeedbackUtil(AntifraudBaseTest testClass, Class<Long> clazz) {
        super(testClass, clazz);
    }

    @Override
    public String getBaseAddress() {
        return null;
    }

    @Override
    protected String getEntityName() {
        return null;
    }

    @Override
    public Function<Long, String> getIdentifier() {
        return null;
    }

    public void feedBack(User user, long trxId, ResultEnum result){
        final HttpRequest httpRequest = HttpRequestBuilder.builder(this)
                .withUser(user).httpRequest();
        //TODO add transaction id to the path
        final HttpResponse send = httpRequest.send();
    }
}
