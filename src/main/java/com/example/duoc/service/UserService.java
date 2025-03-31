package com.example.duoc.service;

import com.example.duoc.model.User;
import com.example.duoc.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Find all users
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // Find user by ID
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    // Find user by username
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Find user by email
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Get the currently authenticated user
    public User getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            System.out.println("Getting current user: " + username);
            
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        } catch (Exception e) {
            System.err.println("Error getting current user: " + e.getMessage());
            throw e;
        }
    }

    // Create a new user
    public User createUser(User user) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Set default role if none provided
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Collections.singleton("ROLE_USER"));
        }
        
        return userRepository.save(user);
    }

    // Update user details
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    // Delete user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Initialize with sample users (for demonstration purposes)
    @PostConstruct
    public void init() {
        // Check if users exist
        if (userRepository.count() == 0) {
            System.out.println("Initializing users database...");
            // Create sample users
            try {
                createSampleUsers();
                System.out.println("Sample users initialized successfully!");
            } catch (Exception e) {
                System.err.println("Error initializing users: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Users already exist, skipping initialization");
        }
    }

    // Create sample users
    private void createSampleUsers() {
        try {
            System.out.println("Initializing users...");
            
            // User 1
            User user1 = new User();
            user1.setUsername("user1");
            user1.setEmail("user1@example.com");
            user1.setPassword(passwordEncoder.encode("password1"));
            user1.setRoles(Set.of("ROLE_USER"));
            userRepository.save(user1);
            System.out.println("Created user: " + user1.getUsername());

            // User 2
            User user2 = new User();
            user2.setUsername("user2");
            user2.setEmail("user2@example.com");
            user2.setPassword(passwordEncoder.encode("password2"));
            user2.setRoles(Set.of("ROLE_USER"));
            userRepository.save(user2);
            System.out.println("Created user: " + user2.getUsername());

            // Admin
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of("ROLE_USER", "ROLE_ADMIN"));
            userRepository.save(admin);
            System.out.println("Created user: " + admin.getUsername());
            
            System.out.println("Sample users initialized successfully!");
        } catch (Exception e) {
            System.err.println("Error creating sample users: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
