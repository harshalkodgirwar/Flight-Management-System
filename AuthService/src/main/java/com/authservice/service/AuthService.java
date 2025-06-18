package com.authservice.service;

//import com.authservice.config.JwtService;
//import com.authservice.model.User;
//import com.authservice.repository.AuthRepo;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.password.PasswordEncoder;
import com.authservice.model.User;
import com.authservice.repository.AuthRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private AuthRepo repo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager manager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User register(User user) {

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }
    public boolean testDatabaseConnection() {
        try {
            // Try a simple query
            List<User> users = repo.findAll();
            System.out.println("Database connection successful. Found " + users.size() + " users");
            return true;
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

//    public String verify(User user) {
//
//        Authentication authentication = manager
//                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
//
//        if(authentication.isAuthenticated()) {
//            // Get user from database to include role in token
//            User authenticatedUser = repo.findByUsername(user.getUsername())
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
//            // Pass role to token generation
//            return JwtService.generateToken(authenticatedUser.getUsername(), authenticatedUser.getRole());
//        }
//        return "fail";
//
//    }

    public String verify(User user) {
        System.out.println("Attempting to authenticate user: " + user.getUsername());

        try {
            Authentication authentication = manager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            System.out.println("Authentication result: " + (authentication.isAuthenticated() ? "SUCCESS" : "FAILED"));

            if (authentication.isAuthenticated()) {
                // Get user from database
                try {
                    User authenticatedUser = repo.findByUsername(user.getUsername())
                            .orElse(null);

                    if (authenticatedUser != null) {
                        System.out.println("User found in database: " + authenticatedUser.getUsername() +
                                ", Role: " + authenticatedUser.getRole());

                        String token = JwtService.generateToken(authenticatedUser.getUsername(),
                                authenticatedUser.getRole());
                        System.out.println("Generated token of length: " + token.length());
                         return JwtService.generateToken(authenticatedUser.getUsername(), authenticatedUser.getRole());
                    } else {
                        System.err.println("User authenticated but not found in database: " + user.getUsername());
                        return "fail-user-not-found";
                    }
                } catch (Exception e) {
                    System.err.println("Error retrieving authenticated user: " + e.getMessage());
                    e.printStackTrace();
                    return "fail-db-error";
                }
            }
        } catch (Exception e) {
            System.err.println("Authentication exception: " + e);
        }
        return null;
    }



//    public AuthService(AuthenticationManager authenticationManager, AuthRepo repo, PasswordEncoder passwordEncoder, JwtService jwtService) {
//        this.authenticationManager = authenticationManager;
//        this.repo = repo;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtService = jwtService;
//    }
//
//    private final AuthRepo repo;
//
//    private final PasswordEncoder passwordEncoder;
//
//    private final JwtService jwtService;
//    private final AuthenticationManager authenticationManager;
//
//    public Map<String, String> register(String username, String password) {
//        if (repo.findByUsername(username).isPresent()) {
//            throw new RuntimeException("Username already exists");
//        }
//
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(passwordEncoder.encode(password));
//        user.setRole("ROLE_USER");
//
//
//        repo.save(user);
//
//        String jwtToken = jwtService.generateToken(user);
//        return Map.of("token", jwtToken);
//    }
//
//    public Map<String, String> authenticate(String username, String password) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(username, password)
//        );
//
//        if (!authentication.isAuthenticated()) {
//            throw new RuntimeException("Invalid credentials");
//        }
//
//        var user = repo.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        String jwtToken = jwtService.generateToken(user);
//        return Map.of("token", jwtToken);
//    }
//
//    public boolean validateToken(String token) {
//        return jwtService.validateToken(token);
//    }
}