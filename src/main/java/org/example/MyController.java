package org.example;

import org.example.model.Property;
import org.example.repos.PropertyRepository;
import org.example.repos.UserRepository;
import org.example.service.NewsService;
import org.example.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import static org.example.job.AuthCheker.isAdmin;
import static org.example.job.AuthCheker.isAuth;

@Controller
public class MyController {
    @Autowired
    private NewsService newsService;
    @Autowired
    private UserRepository userRepository;
    private final PropertyService propertyService;
    @Autowired
    private PropertyRepository propertyRepository;
    public MyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }
    @GetMapping("/property")
    public String getProperty(Model model, Authentication authentication) {
        List<Property> properties = propertyRepository.findAll();
        model.addAttribute("properties", properties);
        model.addAttribute("showLogout", isAuth(authentication));
        model.addAttribute("ADMIN", isAdmin(authentication));
        if(isAuth(authentication)) {
            model.addAttribute("name", authentication.getName());
        }
        return "property"; // Имя HTML-шаблона, который мы создадим
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
    @GetMapping("/property/{name}_{id}")
    public String getSpecificProperties(@PathVariable Long id, @PathVariable String name,
                                        Model model, Authentication authentication) {
        Property property = propertyRepository.findById(id).orElse(null);
        model.addAttribute("property", property);
        model.addAttribute("showLogout", isAuth(authentication));
        model.addAttribute("ADMIN", isAdmin(authentication));
        if(isAuth(authentication)) {
            model.addAttribute("name", authentication.getName());
        }
        return "property-details";
    }
    @GetMapping("/addProperty")
    public String addProperty(Model model, Authentication authentication) {
        model.addAttribute("showLogout", isAuth(authentication));
        model.addAttribute("isPost", false);
        if(isAuth(authentication)) {
            model.addAttribute("name", authentication.getName());
        }
        return "addProperty";
    }
    @GetMapping("/news")
    public String getAllNews(Model model, Authentication authentication) {
        model.addAttribute("showLogout", isAuth(authentication));
        model.addAttribute("ADMIN", isAdmin(authentication));
        if(isAuth(authentication)) {
            model.addAttribute("name", authentication.getName());
        }
        return "news";
    }
    @PostMapping("/addProperty")
    public String addPropertyPost(Model model, String name, Double cost, String description, double latitude, double longitude, Authentication authentication) {
        model.addAttribute("showLogout", isAuth(authentication));
        if(isAuth(authentication)) {
            model.addAttribute("name", authentication.getName());
        }
        try {
            propertyService.addProperty(name, description, cost, latitude, longitude);
            model.addAttribute("isPost", true);
            return "addProperty";
        }
        catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "addProperty";
        }
    }
}

