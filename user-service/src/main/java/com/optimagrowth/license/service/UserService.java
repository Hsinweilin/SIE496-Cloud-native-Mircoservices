package com.optimagrowth.license.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;


import com.optimagrowth.license.config.ServiceConfig;
import com.optimagrowth.license.model.User;
import com.optimagrowth.license.repository.UserRepository;
import com.optimagrowth.license.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;  // Autowire JWT utility class

    @Autowired
    private PasswordEncoder passwordEncoder; // Spring will inject the PasswordEncoder bean defined in SecurityConfig

	@Autowired
	ServiceConfig config;

    // get user by id
    public User getUser(Long userId){
        return userRepository.findById(userId).orElse(null);
    }

    // get user by email
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    // create user
    public User createUser(User user){
        // Hash the password before storing it
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword); // Store the encoded password
        return userRepository.save(user);  // Save the user to the DB
    }

    // update user
    public User updateUser(Long userId, User updateduser){
        User user = getUser(userId);
        user.setUsername(updateduser.getUsername());
        user.setEmail(updateduser.getEmail());
        user.setPassword(updateduser.getPassword());
        return userRepository.save(user);
    }

	// delete user
    public HttpStatus deleteUser(Long userId){
        try {
            // Check if the user exists
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return HttpStatus.NOT_FOUND; // User not found
            }
            // Delete the user if it exists
            userRepository.delete(user);
            return HttpStatus.OK; // User successfully deleted
        } catch (Exception e) {
            // Handle any unexpected errors
            return HttpStatus.INTERNAL_SERVER_ERROR; // Internal server error
        }
    }

    // authenticate and generate token
    public String authenticateAndGenerateToken(String username, String password) {
        // Fetch user by username
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // Password matches, generate JWT token
            return jwtUtil.generateToken(username);
        }
        // Invalid credentials
        return null;
    }
}
