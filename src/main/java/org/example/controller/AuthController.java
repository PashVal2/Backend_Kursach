package org.example.controller;

import org.example.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import static org.example.job.AuthCheker.isAuth;

@Controller
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/register") // GET-запрос страници регистрации
    public String showRegistrationPage(Model model, Authentication authentication) {
        model.addAttribute("showLogout", isAuth(authentication));
        if(isAuth(authentication)) {
            model.addAttribute("name", authentication.getName());
        }
        return "register";
    }
    @PostMapping("/register") // POST-запрос для добавления нового юзера
    public String registerUser(String username, String password, Authentication authentication,
           String confirmPassword, Model model) {
        model.addAttribute("showLogout", isAuth(authentication));
        if(isAuth(authentication)) {
            model.addAttribute("name", authentication.getName());
        }
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Пароли не совападают");
            return "register";
        }
        try {
            userService.register(username, password);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Пользователь с таким именем уже существует");
            return "register";
        }
    }
    @GetMapping("/login") // GET-запрос страници входа
    public String showLoginPage(Model model, Authentication authentication) {
        model.addAttribute("showLogout", isAuth(authentication));
        if(isAuth(authentication)) {
            model.addAttribute("name", authentication.getName());
        }
        return "login";
    }
}
