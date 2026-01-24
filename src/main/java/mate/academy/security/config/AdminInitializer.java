package mate.academy.security.config;

import lombok.RequiredArgsConstructor;
import mate.academy.security.model.User;
import mate.academy.security.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private static final String ADMIN_EMAIL = "admin@security.com";
    private static final String ADMIN_RAW_PASSWORD = "admin1234";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        User admin = userRepository.findByEmail(ADMIN_EMAIL)
                .orElseThrow(() -> new IllegalStateException("Admin not found"));

        admin.setPassword(passwordEncoder.encode(ADMIN_RAW_PASSWORD));
        userRepository.save(admin);
    }
}
