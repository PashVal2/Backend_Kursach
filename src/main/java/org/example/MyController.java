package org.example;

import org.example.model.Property;
import org.example.repos.PropertyRepository;
import org.example.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class MyController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PropertyRepository propertyRepository;
    @GetMapping("/property")
    public String getProperty(Model model, Authentication authentication) {
        // Получаем всех пользователей из базы данных
        List<Property> properties = propertyRepository.findAll();
        model.addAttribute("properties", properties);
        model.addAttribute("showLogout", isAuth(authentication));
        return "property"; // Имя HTML-шаблона, который мы создадим
    }
    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        model.addAttribute("message", "b");
        model.addAttribute("showLogout", isAuth(authentication));
        return "index";
    }
    @GetMapping("/property/{id}")
    public String getSpecificProperties(@PathVariable Long id, Model model, Authentication authentication) {
        Property property = propertyRepository.findById(id).orElse(null);
        model.addAttribute("property", property);
        model.addAttribute("showLogout", isAuth(authentication));
        return "property-details";
    }
    public boolean isAuth(Authentication authentication) {
        if (authentication != null &&
                authentication.isAuthenticated()) {
            return true;
        } else {
            return false;
        }
    }
}

