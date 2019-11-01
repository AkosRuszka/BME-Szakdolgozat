package hu.bme.akosruszka.authenticationmanager.helper;

import hu.bme.akosruszka.authenticationmanager.dao.UserRepository;
import hu.bme.akosruszka.authenticationmanager.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public DatabaseInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        User user = User.builder()
                .email("akosruszka@gmail.com")
                .innerEmail("akosruszka@gmail.com")
                .build();
        userRepository.save(user);
    }
}
