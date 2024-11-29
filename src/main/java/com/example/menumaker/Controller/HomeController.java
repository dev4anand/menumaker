package com.example.menumaker.Controller;


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

    @PostMapping("/signup")
    public String handleSignup(@ModelAttribute(name = "signupForm") @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("mobileNumber") String mobileNumber, Model model) {

        // Logic to process the user data
        System.out.println("email: " + email);
        System.out.println("Password: " + password);
        System.out.println("Mobile Number: " + mobileNumber);

        // Add data to the model if needed
        model.addAttribute("message", "User registered successfully!");
        try {
            // Create a new User object
            User user = new User();
            user.setUsername(mobileNumber);
            user.setEmail(email);
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            System.out.println(hashedPassword);
            user.setPasswordHash(hashedPassword);
            user.setActive(true);
            Long roleId = 2L; // Set the desired role ID
            MasterUserRole role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + roleId));

            // Set the role for the user
            user.setRole(role);
            // Save the user to the database
            userRepository.save(user);
            List<User> roleSpecificUsers = userRepository.findAllByRole_Id(2L);
            model.addAttribute("usersWithRoleId2", roleSpecificUsers);
            return "users/dashboard";

        } catch (Exception e) {
            // Log the exception (optional, but recommended)
            System.err.println("Error saving user to database: " + e.getMessage());

            // Add error message to the model
            model.addAttribute("error", "An error occurred while registering the user. Please try again.");
            // Redirect back to the signup form
            return "users/createuser"; // Return to the form with the error message
        }
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<String> editUser(@PathVariable Long id) {
        // Perform your edit logic here (e.g., fetching or updating the user)
        return ResponseEntity.ok("User with ID " + id + " has been edited successfully.");
    }

    @PostMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Long id, Model model) {
        logger.info("User id to delete: {}", id);
        List<User> remainingUsers = userRepository.findAllByRole_Id(2L);
        logger.info("Users list: {}", remainingUsers);
        model.addAttribute("usersWithRoleId2", remainingUsers);
        // Redirect to dashboard with error message
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            model.addAttribute("error", "User with ID " + id + " not found."); // Inform user if not found
        }
        return "users/dashboard"; // Redirect to dashboard
    }
//@PostMapping("/delete-user/{id}")
//@ResponseBody
//public ResponseEntity<String> deleteUser(@PathVariable Long id) {
//    logger.info("User ID to delete: {}", id);
//
//    if (userRepository.existsById(id)) {
//        userRepository.deleteById(id);
//        return ResponseEntity.ok("User deleted successfully.");
//    }
//
//    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
//}


}

