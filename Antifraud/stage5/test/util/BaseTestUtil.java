package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BaseTestUtil {

    protected final AntifraudBaseTest testClass;

    private ObjectMapper objectMapper = new ObjectMapper();

    public BaseTestUtil(AntifraudBaseTest testClass) {
        this.testClass = testClass;
    }


    public String toJson(Object onject) {
        try {
            return objectMapper.writeValueAsString(onject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T fromJson(HttpResponse response, Class<T> clazz) {
        try {
            return objectMapper.readValue(response.getContent(), clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    //TODO potential spoiler. remove it
    public String hashPassword(String password){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(password.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter
                .printHexBinary(digest).toUpperCase();
    }
}
