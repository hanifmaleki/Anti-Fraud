package antifraud;


import antifraud.model.Role;
import antifraud.model.User;
import antifraud.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
public class AntifraudApplication {
    public static void main(String[] args) {
        final ConfigurableApplicationContext applicationContext = SpringApplication.run(AntifraudApplication.class, args);

        final User admin = new User("admin", "admin", Role.ADMIN, "admin");

        final UserService userService = applicationContext.getBean(UserService.class);

        userService.addUser(admin);
    }
}
