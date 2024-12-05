package org.example.controller;

import org.example.model.Property;
import org.example.service.PropertyService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.example.job.AuthCheker.isAdmin;
import static org.example.job.AuthCheker.isAuth;

@Controller
public class PropertyController {
    private PropertyService propertyService;
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }
    @GetMapping("/property")  // Метод для обработки GET-запроса на страницу
    public String getProperty(Model model, Authentication authentication) {
        List<Property> properties = propertyService.findAll();
        model.addAttribute("properties", properties);
        model.addAttribute("showLogout", isAuth(authentication));
        model.addAttribute("ADMIN", isAdmin(authentication));
        if(isAuth(authentication)) {
            model.addAttribute("name", authentication.getName());
        }
        return "property"; // Имя HTML-шаблона, который мы создадим
    }
    @DeleteMapping("/deleteProperty") // Метод для обработки DELETE-запроса на удаление объекта недвижимости
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
