import antifraud.model.Role;
import antifraud.model.User;

public class TestDataProvider {

    public static final String BASE_ADDRESS = "/api/antifraud";
    public static final String IP_ADDRESS = BASE_ADDRESS + "/stolencard";
    public static final String STOLEN_ADDRESS = BASE_ADDRESS + "/suspicious-ip";
    public static final String TRX_ADDRESS = BASE_ADDRESS + "/transaction";
    public static final String USER_ADDRESS = BASE_ADDRESS + "/user";

    private final User user1;
    private final User user2;
    private final User userWithoutRole;
    private final User userWithoutName;
    private final User userWithoutUsername;

    public TestDataProvider() {
        user1 = User
                .builder()
                .name("John Doe")
                .username("john_doe")
                .role(Role.USER)
                .build();

        user2 = User
                .builder()
                .name("Richard Roe")
                .username("richard_roe")
                .role(Role.ADMIN)
                .build();

        userWithoutRole = User
                .builder()
                .name("John Doe")
                .username("john_doe")
                .build();

        userWithoutName = User
                .builder()
                .username("john_doe")
                .role(Role.USER)
                .build();

        userWithoutUsername = User
                .builder()
                .username("john_doe")
                .role(Role.USER)
                .build();
    }
}
