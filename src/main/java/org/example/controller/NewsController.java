package org.example.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static org.example.job.AuthCheker.isAdmin;
import static org.example.job.AuthCheker.isAuth;

@Controller
public class NewsController {
    @GetMapping("/news") // Обрабатывает GET-запрос для отображения страницы с новостями
    public String getAllNews(Model model, Authentication authentication) {
        model.addAttribute("showLogout", isAuth(authentication));
        model.addAttribute("ADMIN", isAdmin(authentication));
        if(isAuth(authentication)) {
            model.addAttribute("name", authentication.getName());
        }
        return "news";
    }
}
