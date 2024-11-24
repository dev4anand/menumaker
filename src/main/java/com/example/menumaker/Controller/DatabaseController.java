package com.example.menumaker.Controller;


import com.example.menumaker.model.User;
import com.example.menumaker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class DatabaseController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/test-connection")
    public String testDatabaseConnection() {

        try {
            User firstUser = userRepository.findById(2L).orElse(null);

            // Check if a user was found
            if (firstUser != null) {
                // Access the values and return a success message
                return "Connected to the database successfully! First User: " + firstUser.getUsername() + ", Email: " + firstUser.getEmail();
            } else {
                return "Database connection successful, but no users found.";
            }
        } catch (Exception e) {
            // Handle any exception that occurs during the database operation
            return "Database connection failed: " + e.getMessage();
        }
    }

}

//    @GetMapping("/user/{id}")
//    public User getUserById(@PathVariable Long id) {
//        // Fetch user by ID from the repository
//        return userRepository.findById(id).orElse(null); // Return null if not found
//    }
