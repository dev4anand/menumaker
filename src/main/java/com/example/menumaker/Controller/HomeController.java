package com.example.menumaker.Controller;


import com.example.menumaker.dto.UserDto;
import com.example.menumaker.model.MasterUserRole;
import com.example.menumaker.model.User;
import com.example.menumaker.repository.MasterUserRoleRepository;
import com.example.menumaker.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MasterUserRoleRepository roleRepository;

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/dashboard")
    public String login(@ModelAttribute(name = "loginForm") @RequestParam String username, @RequestParam String password, Model model) {


        logger.info("Received login request with username: {} and password: {}", username, password);


        Optional<User> userOptional = userRepository.findOneByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (BCrypt.checkpw(password, user.getPasswordHash())) {
                model.addAttribute("message", "Login successful!");

                List<User> roleSpecificUsers = userRepository.findAllByRole_Id(2L);
                logger.info("Users with role ID 2: {}", roleSpecificUsers);
                model.addAttribute("usersWithRoleId2", roleSpecificUsers);

                return "users/dashboard";
            } else {
                model.addAttribute("error", "Incorrect Username & Password");
                return "index";
            }
        } else {
            model.addAttribute("error", "User not found!");
            return "index";
        }
    }


    @GetMapping("/create-user")
    public String createUser() {
        return "users/createuser";
    }

//    @PostMapping("/signup")
//    public String handleSignup(@ModelAttribute(name = "signupForm") @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("mobileNumber") String mobileNumber, Model model) {
//
//        // Logic to process the user data
//        System.out.println("email: " + email);
//        System.out.println("Password: " + password);
//        System.out.println("Mobile Number: " + mobileNumber);
//
//        // Add data to the model if needed
//        model.addAttribute("message", "User registered successfully!");
//        try {
//            // Create a new User object
//            User user = new User();
//            user.setUsername(mobileNumber);
//            user.setEmail(email);
//            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
//            System.out.println(hashedPassword);
//            user.setPasswordHash(hashedPassword);
//            user.setActive(true);
//            Long roleId = 2L; // Set the desired role ID
//            MasterUserRole role = roleRepository.findById(roleId)
//                    .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + roleId));
//
//            // Set the role for the user
//            user.setRole(role);
//            // Save the user to the database
//            userRepository.save(user);
//            List<User> roleSpecificUsers = userRepository.findAllByRole_Id(2L);
//            model.addAttribute("usersWithRoleId2", roleSpecificUsers);
//            return "users/dashboard";
//
//        } catch (Exception e) {
//            // Log the exception (optional, but recommended)
//            System.err.println("Error saving user to database: " + e.getMessage());
//
//            // Add error message to the model
//            model.addAttribute("error", "An error occurred while registering the user. Please try again.");
//            // Redirect back to the signup form
//            return "users/createuser"; // Return to the form with the error message
//        }
//    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<String> editUser(@PathVariable Long id) {
        // Perform your edit logic here (e.g., fetching or updating the user)
        return ResponseEntity.ok("User with ID " + id + " has been edited successfully.");
    }

    @PostMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, Model model) {
        logger.info("User id to delete: {}", id);

        // Check if user exists
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            logger.info("User deleted successfully");
            return ResponseEntity.ok(new ApiResponse(true, "User deleted successfully."));
        } else {
            logger.warn("User not found: {}", id);
            return ResponseEntity.status(404).body(new ApiResponse(false, "User with ID " + id + " not found."));
        }
    }

    @PostMapping("/createusers")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        logger.info("User  details");
        logger.info("Users dto: {}", userDto);
        try {
            User user = new User();
            user.setUsername(userDto.getMobile());
            user.setEmail(userDto.getEmail());
            String hashedPassword = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt());
            System.out.println(hashedPassword);
            user.setPasswordHash(hashedPassword);
            user.setActive(true);
            Long roleId = 2L; // Set the desired role ID
            MasterUserRole role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + roleId));

            user.setRole(role);
            userRepository.save(user);
            return ResponseEntity.ok(new ApiResponse(true, "User created successfully."));
        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
            return ResponseEntity.status(404).body(new ApiResponse(false, "Cant Create user"));

        }
    }

    // API response format
    static class ApiResponse {
        private boolean success;
        private String message;

        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }

}

