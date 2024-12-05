package org.example;

import org.example.model.Property;
import org.example.repos.PropertyRepository;
import org.example.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.example.job.AuthCheker.isAdmin;
import static org.example.job.AuthCheker.isAuth;

@Controller
public class PropertyController {
    @Autowired
    private PropertyRepository propertyRepository;
    private PropertyService propertyService;
    public PropertyController(PropertyRepository propertyRepository, PropertyService propertyService) {
        this.propertyRepository = propertyRepository;
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
    @PostMapping("/deleteProperty")
    public String deleteProperty(@RequestParam("property_id") Long propertyId, Model model) {
        try {
            propertyService.deleteById(propertyId);
            model.addAttribute("message", "Объект успешно удалён.");
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при удалении объекта: " + e.getMessage());
        }
        return "redirect:/property";
    }
}
