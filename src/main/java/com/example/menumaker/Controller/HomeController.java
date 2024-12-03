package com.example.menumaker.Controller;


import com.example.menumaker.dto.UserDto;
import com.example.menumaker.model.*;
import com.example.menumaker.repository.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class HomeController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopUserRepository shopUserRepository;

    @Autowired
    private MasterUserRoleRepository roleRepository;
    @Autowired
    private MasterCurrencyRepository masterCurrencyRepository;

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
                if (user.getRole().getId() == 1) {
                    List<ShopUser> shopsUser = shopUserRepository.findAll();
                    model.addAttribute("message", "Login successful!");
//                  logger.info("Users det: {}", user);
                    model.addAttribute("shopsList", shopsUser);
                    return "design/module/sample";
                } else {
                    return "design/module/sample";
                }
            } else {
                model.addAttribute("error", "Incorrect Username & Password");
                return "index";
            }
        } else {
            model.addAttribute("error", "User not found!");
            return "index";
        }
    }


    @PostMapping("/CreateRestaurant")
    public ResponseEntity<?> createRestaurant(@RequestBody UserDto userDto) {
        logger.info("form data: {}", userDto);
        try {

            User user = new User();
            user.setUsername(userDto.getMobile());
            user.setEmail(userDto.getEmail());
            String hashedPassword = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt());
            user.setPasswordHash(hashedPassword);
            user.setActive(true);
            Long roleId = 2L; // Set the desired role ID
            MasterUserRole role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + roleId));
            user.setRole(role);
            userRepository.save(user);
            Shop shop = new Shop();
            shop.setName(userDto.getShopName());
            shop.setAddress(userDto.getShopAddress());
            shop.setActive(true);
            Long Id = 1L;
            MasterCurrency currency = masterCurrencyRepository.findById(Id)
                    .orElseThrow(() -> new IllegalArgumentException("Currency not found with ID: " + Id));
            shop.setMasterCurrency(currency);
            shop.setCreatedBy(1);
            shopRepository.save(shop);
            ShopUser shopUser = new ShopUser();
            shopUser.setUser(user);
            shopUser.setRole(role);
            shopUser.setShop(shop);
            shopUser.setCreatedBy(1);
            shopUser.setActive(true);
            shopUserRepository.save(shopUser);
            return ResponseEntity.ok(new ApiResponse(true, "Shop and User created successfully.", null));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ApiResponse(false, "Cant Create user", null));
        }
    }


    @PostMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, Model model) {
        Optional<ShopUser> shopUser = shopUserRepository.findOneById(id);
        if (shopUserRepository.existsById(id)) {
            if (shopUser.isPresent()) {
                ShopUser user = shopUser.get();
                Long shopId = user.getShop().getId();
                Long UserId = user.getUser().getId();
                Optional<User> userDetails = userRepository.findOneById(UserId);
                Optional<ShopUser> shopUserDetails = shopUserRepository.findOneById(id);
                Optional<Shop> shopDetails = shopRepository.findOneById(shopId);
                shopUserRepository.deleteById(id);
                shopRepository.deleteById(shopId);
                userRepository.deleteById(UserId);
                logger.info("User deleted successfully");
                return ResponseEntity.ok(new ApiResponse(true, "User deleted successfully.", null));
            }else{
                logger.warn("Error finding User: {}", id);
                return ResponseEntity.status(404).body(new ApiResponse(false, "Error finding User with ID " + id + " not found.", null));
            }
        } else {
            logger.warn("User not found: {}", id);
            return ResponseEntity.status(404).body(new ApiResponse(false, "User with ID " + id + " not found.", null));
        }
    }

    @PostMapping("/CreateUser")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        try {
            User user = new User();
            user.setUsername(userDto.getMobile());
            user.setEmail(userDto.getEmail());
            String hashedPassword = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt());
            user.setPasswordHash(hashedPassword);
            user.setActive(true);
            Long roleId = 2L; // Set the desired role ID
            MasterUserRole role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + roleId));
            user.setRole(role);
            userRepository.save(user);
            return ResponseEntity.ok(new ApiResponse(true, "User created successfully.", null));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ApiResponse(false, "Cant Create user", null));
        }
    }
    @PostMapping("/edit-user/{id}")
    public ResponseEntity<?> editUserModal(@PathVariable Long id) {
        Optional<ShopUser> optionalUser = shopUserRepository.findOneById(id);
        if (optionalUser.isPresent()) {
            ShopUser user = optionalUser.get();
            Map<String, Object> response = new HashMap<>();
            Map<String, String> userDetails = new HashMap<>();
            Map<String, String> shopDetails = new HashMap<>();

            userDetails.put("email", user.getUser().getEmail());
            userDetails.put("mobile", user.getUser().getUsername());
            shopDetails.put("name", user.getShop().getName());
            shopDetails.put("address", user.getShop().getAddress());

            response.put("user", userDetails);
            response.put("shop", shopDetails);

            logger.info("User details: {}", response);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "User not found.", null));
        }
    }

//    @PostMapping("/edit-user/{id}")
//    public ResponseEntity<?> editUserModal(@PathVariable Long id) {
//        Optional<ShopUser> details = shopUserRepository.findOneById(id);
//        logger.info("Updating user with id: {}", id);
//        if (details.isPresent()) {
//            ShopUser user = details.get();
//            var shopId = user.getShop().getName();
//            var mobileNumber = user.getUser().getUsername();
//            var email = user.getUser().getEmail();
//
//
//            logger.info("userdetails: {}", user);
//            return ResponseEntity.ok(details.get()); // Directly return user details
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiResponse(false, "User not found.", null));
//        }
//    }

    @PostMapping("/UpdateUser/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto, @PathVariable Long id) {
        logger.info("Updating user with id: {}", id);
        try {
            // Retrieve the user by id, and throw exception if not found
            User user = userRepository.findOneById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));

            // Set updated user information
            user.setUsername(userDto.getMobile());
            user.setEmail(userDto.getEmail());

            // Hash the password
            String hashedPassword = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt());
            System.out.println("Hashed Password: " + hashedPassword);
            user.setPasswordHash(hashedPassword);

            // Set the user as active
            user.setActive(true);

            // Assign the user role (assuming role ID is fixed here)
            Long roleId = 2L;
            MasterUserRole role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + roleId));
            user.setRole(role);

            // Save the updated user
            userRepository.save(user);

            return ResponseEntity.ok(new ApiResponse(true, "User updated successfully.", null));
        } catch (IllegalArgumentException e) {
            // Handle specific exceptions like user or role not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, e.getMessage(), null));
        } catch (Exception e) {
             // Generic exception handling
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error updating user", null));
        }
    }

    static class ApiResponse {
        private boolean success;
        private String message;
        private Object data;

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public Object getData() {
            return data;
        }
    }

}

