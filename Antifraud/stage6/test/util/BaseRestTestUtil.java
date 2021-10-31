package util;

import antifraud.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;

import java.lang.reflect.Array;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

public abstract class BaseRestTestUtil<T> {

    protected final AntifraudBaseTest testClass;
    private final Class<T> clazz;
    private final User defaultAdmin;

    private ObjectMapper objectMapper = new ObjectMapper();


    public BaseRestTestUtil(AntifraudBaseTest testClass, Class<T> clazz) {
        this.testClass = testClass;
        this.clazz = clazz;
        this.defaultAdmin = testClass.getDefaultAdmin();
    }


    protected AntifraudBaseTest getTestClass() {
        return testClass;
    }

    protected Class<T> getClazz() {
        return clazz;
    }

    public abstract String getBaseAddress();

    protected abstract String getEntityName();

    public List<T> get() {
        return this.get(defaultAdmin);
    }

    public List<T> get(User user) {
        String message = String.format("Could not get list of %s.", getEntityName());
        final String content = sendGetAndExpect(user, 200, message);
        return listFromJson(content);
    }


    public T getByIdentifier(String identifier) {
        return getByIdentifier(identifier, defaultAdmin);
    }

    public T getByIdentifier(String identifier, User user) {
        String message = String.format("Could not get %s with identifier %s.", getEntityName(), identifier);
        final String body = sendGetOneAndExpect(identifier, user, 200, message);
        return fromJson(body);
    }

    public void add(T element) {
        this.add(element, defaultAdmin);
    }

    public void add(T element, User user) {
        String message = String.format("Could not add %s %s.", getEntityName(), element.toString());
        sendPostRequestAndExpect(element, user, 201, message);
    }

    public void remove(String identifier) {
        this.remove(identifier, defaultAdmin);
    }

    public void remove(String identifier, User user) {
        String message = String.format("Could not remove existing %s with identifier %s.", getEntityName(), identifier);
        sendDeleteAndExpect(identifier, user, 200, message);
    }

    public String sendGetAndExpect(int statusCode, String errorMessage) {
        return this.sendGetAndExpect(defaultAdmin, statusCode, errorMessage);
    }

    public String sendGetAndExpect(User user, int statusCode, String errorMessage) {
        return HttpRequestBuilder.builder(this)
                .withUser(user)
                .whenGetList()
                .then()
                .statusCode(statusCode)
                .withErrorMessage(errorMessage)
                .checkAndReturnContent();
    }

    public void sendPostRequestAndExpect(T element, int statusCode, String errorMessage) {
        this.sendPostRequestAndExpect(element, defaultAdmin, statusCode, errorMessage);
    }

    public void sendPostRequestAndExpect(T element, User user, int statusCode, String errorMessage) {
        HttpRequestBuilder.builder(this)
                .withUser(user)
                .whenAdd(element)
                .then()
                .statusCode(statusCode)
                .withErrorMessage(errorMessage)
                .checkAndReturnContent();
    }

    public String sendGetOneAndExpect(String identifier, int statusCode, String errorMessage) {
        return this.sendGetOneAndExpect(identifier, defaultAdmin, statusCode, errorMessage);
    }

    public String sendGetOneAndExpect(String identifier, User user, int statusCode, String errorMessage) {
        return HttpRequestBuilder.builder(this)
                .withUser(user)
                .whenGetOne(identifier)
                .then()
                .statusCode(statusCode)
                .withErrorMessage(errorMessage)
                .checkAndReturnContent();
    }


    public void sendDeleteAndExpect(String identifier, int statusCode, String errorMessage) {
        this.sendDeleteAndExpect(identifier, defaultAdmin, statusCode, errorMessage);
    }

    public void sendDeleteAndExpect(String identifier, User user, int statusCode, String errorMessage) {
        HttpRequestBuilder.builder(this)
                .withUser(user)
                .withErrorMessage(errorMessage)
                .whenRemove(identifier)
                .then()
                .statusCode(statusCode)
                .checkAndReturnContent();
    }

    public void clearAll() {
        this.clearAll(defaultAdmin);
    }

    public void clearAll(User user) {
        final List<T> all = this.get(user);
        all.forEach(
                item -> {
                    final String identifier = getIdentifier().apply(item);
                    this.remove(identifier, user);
                }
        );
    }

    public BiPredicate<T, T> getEqualPredicate() {
        return (t1, t2) -> {
            final String t1Identifier = getIdentifier().apply(t1);
            final String t2Identifier = getIdentifier().apply(t2);
            return t1Identifier.equals(t2Identifier);
        };
    }


    public boolean isAuthorizedSimple(User user) {
        final BaseRestTestUtil<T> util = this;

        return isAuthorized(user, user1 ->
                HttpRequestBuilder.builder(util)
                        .withUser(user1)
                        .whenGetList()
                        .httpResponse());
    }

    public boolean isAuthorized(User user, List<Function<User, HttpResponse>> functions) {
        return functions.stream()
                .map(function -> isAuthorized(user, function))
                .reduce(true, (o1, o2) -> o1 && o2);
    }

    public boolean isAuthorized(User user, Function<User, HttpResponse> function) {
        final HttpResponse response = function.apply(user);
        return response.getStatusCode() < 300;
    }

    public <U> U fromJson(HttpResponse response, Class<U> clazz){
        try {
            return objectMapper.readValue(response.getContent(), clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private T fromJson(String content) {
        try {
            return objectMapper.readValue(content, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<T> listFromJson(String content) {
        final Class<?> aClass = Array.newInstance(clazz, 0).getClass();
        try {
            return (List<T>) objectMapper.readValue(content, aClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract Function<T, String> getIdentifier();
}
