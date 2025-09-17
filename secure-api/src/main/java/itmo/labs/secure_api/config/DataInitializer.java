package itmo.labs.secure_api.config;

import itmo.labs.secure_api.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(
            UserService userService,
            @Value("${INIT_USERNAME:}") String initUsername,
            @Value("${INIT_PASSWORD:}") String initPassword,
            @Value("${INIT_EMAIL:}") String initEmail) {
        return args -> {
            if (initUsername != null && !initUsername.isBlank() && initPassword != null && !initPassword.isBlank()) {
                try {
                    userService.createUser(initUsername, initEmail, initPassword);
                } catch (Exception e) {
                    System.out.println("Error initializing users: " + e.getMessage());
                }
            }
        };
    }
}
