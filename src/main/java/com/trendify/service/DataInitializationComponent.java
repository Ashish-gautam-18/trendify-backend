package com.trendify.service;

import com.trendify.entity.User;
import com.trendify.repository.UserRepository;
import com.trendify.user.domain.UserRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// This component runs automatically on startup to initialize default system data like the Admin user
@Component
public class DataInitializationComponent implements CommandLineRunner {

    private final UserRepository userRepository;
    private CartService cartService;
    private PasswordEncoder passwordEncoder;

    // Constructor injection to wire required database repositories, security encoders, and services
    @Autowired
    public DataInitializationComponent(UserRepository userRepository,
                                       PasswordEncoder passwordEncoder,
                                       CartService cartService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartService = cartService;
    }

    // This method executes automatically right after the Spring Application context starts up completely
    @Override
    public void run(String... args) {
        initializeAdminUser();
    }

    // Checks database and automatically creates a default Admin profile with an empty shopping cart if not present
    private void initializeAdminUser() {
        String adminUsername = "ashish123@gmail.com";

        if (userRepository.findByEmail(adminUsername) == null) {
            User adminUser = new User();

            adminUser.setPassword(passwordEncoder.encode("Ashish@123"));
            adminUser.setFirstName("Ashish");
            adminUser.setLastName("Kumar");
            adminUser.setEmail(adminUsername);
            adminUser.setRole(UserRole.ROLE_ADMIN.toString());

            User admin = userRepository.save(adminUser);

            cartService.createCart(admin);
        }
    }

}
