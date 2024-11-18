package org.example;

import org.example.repos.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register";
    }
    @PostMapping("/register")
    public String registerUser(String username, String password, String confirmPassword, Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }
        try {
            userService.register(username, password);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}
