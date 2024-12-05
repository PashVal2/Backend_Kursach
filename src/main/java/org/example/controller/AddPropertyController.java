package org.example.controller;

import org.example.service.PropertyService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import static org.example.job.AuthCheker.isAuth;

@Controller
public class AddPropertyController {
    private final PropertyService propertyService;
    public AddPropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
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
    @GetMapping("/addProperty")
    public String addProperty(Model model, Authentication authentication) {
        model.addAttribute("showLogout", isAuth(authentication));
        model.addAttribute("isPost", false);
        if(isAuth(authentication)) {
            model.addAttribute("name", authentication.getName());
        }
        return "addProperty";
    }
}
