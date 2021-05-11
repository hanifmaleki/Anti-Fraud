import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;

public class BaseTestUtil {

    protected final SpringTest testClass;

    private ObjectMapper objectMapper = new ObjectMapper();

    public BaseTestUtil(SpringTest testClass) {
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
            T object = objectMapper.readValue(response.getContent(), clazz);
            return object;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
