package data;

import antifraud.model.Role;
import antifraud.model.User;

import static data.TestDataProvider.BASE_ADDRESS;

public class UserDataProvider {

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
    public final User adminUser1WithWrongPassword;


    // Passwords
    public final String adminUser0HashedPassword;
    public final String adminUser1HashedPassword;
    public final String supportUser1HashedPassword;
    public final String basicUser1HashedPassword;
    public final String basicUser2HashedPassword;

    public UserDataProvider() {
        adminUser0 = User
                .builder()
                .name("Admin")
                .username("admin")
                .role(Role.ADMIN)
                .password("admin")
                .build();
        adminUser0HashedPassword = "2d34981e7027c199b6e1c47d1bf4cf8e";

        adminUser1 = User
                .builder()
                .name("John Doe")
                .username("john_doe")
                .role(Role.ADMIN)
                .password("P4ssw0rd")
                .build();
        adminUser1HashedPassword = "$2y$12$kC5OXlEKS2kcjUXg.NVyOeOp8W7OW6zonHkmI3VmXrHu5X129KhFO";


        supportUser1 = User
                .builder()
                .name("Mr. Taxpayer")
                .username("tax_payer")
                .role(Role.USER)
                .password("To8e0rNotTo8e")
                .build();
        supportUser1HashedPassword = "262e81e11f0bcfb860870db336fc2f93";


        basicUser1 = User
                .builder()
                .name("Richard Roe")
                .username("richard_roe")
                .role(Role.USER)
                .password("_vvsH&d$4K")
                .build();
        basicUser1HashedPassword = "faab1c190c8e7c78d580eaa178dd3b25";


        basicUser2 = User
                .builder()
                .name("Joe Public")
                .username("public_joe")
                .role(Role.USER)
                .password("!PFd,52DyB")
                .build();
        basicUser2HashedPassword = "d535c0e2c4d07c502b7a216a4d0d0ab1";


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

        adminUser1WithWrongPassword = User
                .builder()
                .name("John Doe")
                .username("john_doe")
                .role(Role.ADMIN)
                .password("Wrong Password")
                .build();
    }
}
