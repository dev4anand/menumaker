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

import java.util.*;

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
        return "auth/login";
    }

    @PostMapping("/dashboard")
    public String login(@ModelAttribute(name = "loginForm") @RequestParam String username, @RequestParam String password, Model model) {


        logger.info("Received login request with username: {} and password: {}", username, password);
        if ((username == null || username.isEmpty()) && (password == null || password.isEmpty())) {
            model.addAttribute("error", "Please enter a valid username and password");
            return "auth/login";
        }
        Optional<User> userOptional = userRepository.findOneByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (BCrypt.checkpw(password, user.getPasswordHash())) {
                if (user.getRole().getId() == 1) {
                    List<ShopUser> shopsUser = shopUserRepository.findAll();
                    model.addAttribute("message", "Login successful!");
                    model.addAttribute("shopsList", shopsUser);
                    return "users/dashboard";
                } else {
                    return "users/dashboard";
                }
            } else {
                model.addAttribute("error", "Incorrect Username & Password");
                return "auth/login";
            }
        } else {
            model.addAttribute("error", "User not found!");
            return "auth/login";
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


    @PostMapping("/delete-user/{gid}")
    public ResponseEntity<?> deleteUser(@PathVariable String gid, Model model) {
        Optional<ShopUser> shopUser = shopUserRepository.findOneByGid(UUID.fromString(gid));
        if (shopUser.isPresent()) {
            ShopUser user = shopUser.get();
            Long shopId = user.getShop().getId();
            Long UserId = user.getUser().getId();
            Long shpUsr = user.getId();

            Optional<User> userDetails = userRepository.findOneById(UserId);
            Optional<Shop> shopDetails = shopRepository.findOneById(shopId);
            shopUserRepository.deleteById(shpUsr);
            shopRepository.deleteById(shopId);
            userRepository.deleteById(UserId);
            logger.info("User deleted successfully");
            return ResponseEntity.ok(new ApiResponse(true, "User deleted successfully.", null));

        } else {
            logger.warn("User not found: {}", gid);
            return ResponseEntity.status(404).body(new ApiResponse(false, "User with Gid " + gid + " not found.", null));
        }
    }


    @PostMapping("/edit-user/{gid}")
    public ResponseEntity<?> editUserModal(@PathVariable String gid) {
        Optional<ShopUser> optionalUser = shopUserRepository.findOneByGid(UUID.fromString(gid));
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
            response.put("gid", user.getGid());
            logger.info("User details: {}", response);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "User not found.", null));
        }
    }

    @PostMapping("/UpdateUser/{gid}")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto, @PathVariable String gid) {
        logger.info("Updating user with id: {}", gid);
        logger.info("Updating userDto with id: {}", userDto);
        try {
            Optional<ShopUser> shopUser = shopUserRepository.findOneByGid(UUID.fromString(gid));
            if (shopUser.isPresent()) {
                ShopUser user = shopUser.get();
                Long shopId = user.getShop().getId();
                Long UserId = user.getUser().getId();
                Optional<User> userDetails = userRepository.findOneById(UserId);
                User mainUser = userDetails.get();
                mainUser.setUsername(userDto.getMobile());
                mainUser.setEmail(userDto.getEmail());
                String hashedPassword = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt());
                mainUser.setPasswordHash(hashedPassword);
                userRepository.save(mainUser);
                Optional<Shop> shopDetails = shopRepository.findOneById(shopId);
                Shop shop = shopDetails.get();
                shop.setName(userDto.getShopName());
                shop.setAddress(userDto.getShopAddress());
                shopRepository.save(shop);
                return ResponseEntity.ok(new ApiResponse(true, "User updated successfully.", null));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse(false, "Something Went Wrong", null));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, e.getMessage(), null));
        } catch (Exception e) {
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

