package com.example.menumaker.Controller;


import com.example.menumaker.model.User;
import com.example.menumaker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("users", userService.getAllUsers());
//        List<User> users = userService.getAllUsers();
//        model.addAttribute("users", users);
//        log.info("Users: {}", users);
        model.addAttribute("title", "Welcome to My Spring Boot App");
        model.addAttribute("message", "This is the home page of your Spring Boot application.");
        return "home";
    }
}

