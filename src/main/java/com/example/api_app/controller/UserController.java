package com.example.api_app.controller;

import com.example.api_app.model.User;
import com.example.api_app.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Set;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Register Endpoint
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // Check if username is already taken
        if (userService.isUsernameTaken(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already taken");
        }

        // Hash the password before saving (replace with actual password hashing)
        String hashedPassword = hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        // Save the user to the database
        userService.saveUser(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    // Signing Endpoint
    @PostMapping("/signin")
    public ResponseEntity<?> signinUser(@RequestParam String username, @RequestParam String password) {
        // Retrieve user from database based on username
        User user = userService.getUserByUsername(username);
        if (user != null && validatePassword(password, user.getPassword())) {
            // Generate JWT token with user ID and username
            String token = generateJwtToken(user.getId(), user.getUsername());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    // Helper method to hash the password
    private String hashPassword(String password) {
        // Create an instance of BCryptPasswordEncoder
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Hash the password using BCrypt
        String hashedPassword = passwordEncoder.encode(password);

        return hashedPassword;
    }

    // Helper method to validate password
    private boolean validatePassword(String inputPassword, String hashedPassword) {
        // Create an instance of BCryptPasswordEncoder
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Use BCryptPasswordEncoder to check if the input password matches the hashed password
        return passwordEncoder.matches(inputPassword, hashedPassword);
    }

    // Helper method to generate JWT token with user ID and username
    private String generateJwtToken(Long userId, String username) {
        // Set JWT claims with user ID and username
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS512, "ThisIsASampleSecretKeyForJWTTokenGeneration")
                .compact();
    }

    // Get User Full Info and Permissions Endpoint
    @GetMapping("/{userId}/permissions")
    public Set<String> getUserFullInfoAndPermissions(@PathVariable Long userId) {
        return userService.getUserFullInfoAndPermissions(userId);
    }
}
