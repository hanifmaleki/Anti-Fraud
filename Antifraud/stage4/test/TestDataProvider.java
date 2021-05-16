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
    public final User adminUser0;
    public final User adminUser1;
    public final User supportUser1;
    public final User basicUser1;
    public final User basicUser2;
    public final User userWithoutRole;
    public final User userWithoutName;
    public final User userWithoutUsername;

    // Passwords
    public final String adminUser0HashedPassword;
    public final String adminUser1HashedPassword;
    public final String supportUser1HashedPassword;
    public final String basicUser1HashedPassword;
    public final String basicUser2HashedPassword;


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
    public final Transaction trxManually;
    public final Transaction trxProhibited1;
    public final Transaction trxProhibited2;
    public final Transaction trxProhibited3;

    public TestDataProvider() {
        adminUser0 = User
                .builder()
                .name("Admin")
                .username("admin")
                .role(Role.ADMIN)
                .password("jk~7K<cGhTFY(<Q*")
                .build();
        adminUser0HashedPassword = "2d34981e7027c199b6e1c47d1bf4cf8e";

        adminUser1 = User
                .builder()
                .name("John Doe")
                .username("john_doe")
                .role(Role.ADMIN)
                .password("P4ssw0rd")
                .build();
        adminUser1HashedPassword="8efe310f9ab3efeae8d410a8e0166eb2";


        supportUser1 = User
                .builder()
                .name("Mr. Taxpayer")
                .username("tax_payer")
                .role(Role.USER)
                .password("To8e0rNotTo8e")
                .build();
        supportUser1HashedPassword="262e81e11f0bcfb860870db336fc2f93";


        basicUser1 = User
                .builder()
                .name("Richard Roe")
                .username("richard_roe")
                .role(Role.USER)
                .password("_vvsH&d$4K")
                .build();
        basicUser1HashedPassword="faab1c190c8e7c78d580eaa178dd3b25";


        basicUser2 = User
                .builder()
                .name("Joe Public")
                .username("public_joe")
                .role(Role.USER)
                .password("!PFd,52DyB")
                .build();
        basicUser2HashedPassword="d535c0e2c4d07c502b7a216a4d0d0ab1";


        userWithoutRole = User
                .builder()
                .name("Joe Doakes")
                .username("joe_doakes")
                .password("LoveSummer2012")
                .build();

        userWithoutName = User
                .builder()
                .username("john_doe")
                .role(Role.USER)
                .password("Vn8D:_fTMN")
                .build();

        userWithoutUsername = User
                .builder()
                .username("john_doe")
                .role(Role.USER)
                .password("bL57!s^H%+")
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

        trxManually = Transaction
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
