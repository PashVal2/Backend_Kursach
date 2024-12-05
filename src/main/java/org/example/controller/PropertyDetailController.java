package org.example.controller;

import org.example.model.Property;
import org.example.repos.DateRepository;
import org.example.service.DateService;
import org.example.service.PropertyService;
import org.example.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.example.job.AuthCheker.isAdmin;
import static org.example.job.AuthCheker.isAuth;

@Controller
public class PropertyDetailController {
    private final PropertyService propertyService;
    private final UserService userService;
    private final DateService dateService;
    public PropertyDetailController(DateRepository dateRepository, PropertyService propertyService, UserService userService, DateService dateService) {
        this.propertyService = propertyService;
        this.userService = userService;
        this.dateService = dateService;
    }
    @GetMapping("/property/{name}_{id}") // Обрабатывает GET-запрос для получения недвижимости по имени и ID
    public String getSpecificProperties(@PathVariable Long id, @PathVariable String name,
                                        Model model, Authentication authentication) {
        Property property = propertyService.findById(id).orElse(null);
        model.addAttribute("property", property);
        model.addAttribute("showLogout", isAuth(authentication));
        model.addAttribute("ADMIN", isAdmin(authentication));
        if(isAuth(authentication)) {
            model.addAttribute("name", authentication.getName());
        }
        return "property-details";
    }
    @PostMapping("/editProperty")  // Обрабатывает POST-запрос для изменения инофрмации недвижимости
    public String editProperty(
            @RequestParam("id") Long propertyId,
            @RequestParam("name") String name,
            @RequestParam("cost") Double cost,
            @RequestParam("description") String description,
            Model model) {
        try {
            Property property = propertyService.findById(propertyId)
                    .orElseThrow(() -> new IllegalArgumentException("Объект не найден"));
            property.setCost(cost);
            property.setDescription(description);
            propertyService.save(property);
            model.addAttribute("isPost", true);
            model.addAttribute("message", "Изменения успешно сохранены.");
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при редактировании: " + e.getMessage());
        }
        return "redirect:/property";
    }
}
