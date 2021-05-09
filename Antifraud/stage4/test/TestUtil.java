import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;

public class TestUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Object onject) {
        try {
            return objectMapper.writeValueAsString(onject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(HttpResponse response, Class<T> clazz) {
        try {
            T object = objectMapper.readValue(response.getContent(), clazz);
            return object;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
