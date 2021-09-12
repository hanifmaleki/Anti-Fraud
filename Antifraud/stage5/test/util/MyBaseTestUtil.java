package util;

import antifraud.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Array;
import java.util.List;

public abstract class MyBaseTestUtil<T> {

    protected final AntifraudBaseTest testClass;
    private final Class<T> clazz;

    private ObjectMapper objectMapper = new ObjectMapper();


    public MyBaseTestUtil(AntifraudBaseTest testClass, Class<T> clazz) {
        this.testClass = testClass;
        this.clazz = clazz;
    }


    protected AntifraudBaseTest getTestClass() {
        return testClass;
    }

    protected Class<T> getClazz() {
        return clazz;
    }

    public abstract String getBaseAddress();

    protected abstract String getEntityName();

    public List<T> get(User user) {
        String message = String.format("Could not get list of %s.", getEntityName());
        final String content = sendGetAndExpect(user, 200, message);
        return listFromJson(content);
    }

    public T getByIdentifier(String identifier, User user) {
        String message = String.format("Could not get %s with identifier %s.", getEntityName(), identifier);
        final String body = sendGetOneAndExpect(identifier, user, 200, message);
        return fromJson(body);
    }

    public void add(T element, User user) {
        String message = String.format("Could not add %s %s.", getEntityName(), element.toString());
        sendPostRequestAndExpect(element, user, 201, message);
    }

    public void remove(String identifier, User user) {
        String message = String.format("Could not remove existing %s with identifier %s.", getEntityName(), identifier);
        sendDeleteAndExpect(identifier, user, 200, message);
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

    public void sendPostRequestAndExpect(T element, User user, int statusCode, String errorMessage) {
        HttpRequestBuilder.builder(this)
                .withUser(user)
                .whenAdd(element)
                .then()
                .statusCode(statusCode)
                .withErrorMessage(errorMessage)
                .checkAndReturnContent();
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


    public void sendDeleteAndExpect(String identifier, User user, int statusCode, String errorMessage) {
        HttpRequestBuilder.builder(this)
                .withUser(user)
                .withErrorMessage(errorMessage)
                .whenRemove(identifier)
                .then()
                .statusCode(statusCode)
                .checkAndReturnContent();
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

}
