package org.example;

import org.example.repos.UserRepository;
import org.example.service.NewsService;
import org.example.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static org.example.job.AuthCheker.isAdmin;
import static org.example.job.AuthCheker.isAuth;

@Controller
public class IndexController {
    @Autowired
    private NewsService newsService;
    @Autowired
    private UserRepository userRepository;
    private final PropertyService propertyService;
    public IndexController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }
    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        model.addAttribute("message", "b");
        model.addAttribute("showLogout", isAuth(authentication));
        model.addAttribute("ADMIN", isAdmin(authentication));
        if(isAuth(authentication)) {
            model.addAttribute("name", authentication.getName());
        }
        return "index";
    }
}

