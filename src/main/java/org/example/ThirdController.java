package org.example;

import org.example.model.Property;
import org.example.service.PropertyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ThirdController {
    private PropertyService propertyService;
    public ThirdController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }
    @RequestMapping("/accessDenied")
    public String accessDenied() {
        return "accessDenied";
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
    @PostMapping("/editProperty")
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
