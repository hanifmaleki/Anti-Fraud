import antifraud.model.Role;
import antifraud.model.Transaction;
import antifraud.model.User;

public class TestDataProvider {

    public static final String BASE_ADDRESS = "/api/antifraud";
    public static final String IP_ADDRESS = BASE_ADDRESS + "/stolencard";
    public static final String STOLEN_ADDRESS = BASE_ADDRESS + "/suspicious-ip";
    public static final String TRX_ADDRESS = BASE_ADDRESS + "/transaction";
    public static final String USER_ADDRESS = BASE_ADDRESS + "/user";

    // Users
    public final User user1;
    public final User user2;
    public final User userWithoutRole;
    public final User userWithoutName;
    public final User userWithoutUsername;


    // Cards
    public final String stolenCard1;
    public final String stolenCard2;
    public final String okCard;

    // IPs
    public final String suspiciousIp1;
    public final String suspiciousIp2;
    public final String okIp;

    //Transactions
    public final Transaction trxAllowed;
    public final Transaction trxManuall;
    public final Transaction trxProhibited1;
    public final Transaction trxProhibited2;
    public final Transaction trxProhibited3;

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


        stolenCard1 = "2223-0031-2200-3222";
        stolenCard2 = "5200-8282-8282-8210";
        okCard = "5105-1051-0510-5100";
        suspiciousIp1 = "192.168.0.12";
        suspiciousIp2 = "198.18.0.6";
        okIp = "172.16.0.9";

        trxAllowed = Transaction
                .builder()
                .amount(60)
                .ipAddress(okIp)
                .cardSerial(okCard)
                .build();

        trxManuall = Transaction
                .builder()
                .amount(400)
                .ipAddress(okIp)
                .cardSerial(okCard)
                .build();

        trxProhibited1 = Transaction
                .builder()
                .amount(60)
                .ipAddress(okIp)
                .cardSerial(stolenCard2)
                .build();

        trxProhibited2 = Transaction
                .builder()
                .amount(400)
                .ipAddress(suspiciousIp2)
                .cardSerial(okCard)
                .build();

        trxProhibited3 = Transaction
                .builder()
                .amount(2001)
                .ipAddress(suspiciousIp1)
                .cardSerial(stolenCard1)
                .build();
    }
}
