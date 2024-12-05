package org.example;

import org.example.model.Dates;
import org.example.model.Property;
import org.example.model.User;
import org.example.repos.DateRepository;
import org.example.repos.PropertyRepository;
import org.example.repos.UserRepository;
import org.example.service.DateService;
import org.example.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.job.AuthCheker.isAdmin;
import static org.example.job.AuthCheker.isAuth;

@Controller
public class PropertyDetailController {
    @Autowired
    private PropertyRepository propertyRepository;
    private final DateRepository dateRepository;
    private PropertyService propertyService;
    private final UserRepository userRepository;
    public PropertyDetailController(PropertyRepository propertyRepository, DateRepository dateRepository, PropertyService propertyService, DateService dateService, UserRepository userRepository) {
        this.propertyRepository = propertyRepository;
        this.dateRepository = dateRepository;
        this.propertyService = propertyService;
        this.userRepository = userRepository;
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
    @GetMapping("/book-dates/{propertyId}")
    public ResponseEntity<List<Map<String, Object>>> TransitDates(
            @PathVariable Long propertyId, Authentication authentication) {
        List<Dates> dates = dateRepository.findByPropertyId(propertyId);
        User user = userRepository.findByName(authentication.getName()).get();

        List<Map<String, Object>> maps = new ArrayList<>();
        for (Dates date: dates) {
            Map<String, Object> map = new HashMap<>();
            map.put("day", date.getDay());
            map.put("year", date.getYear());
            map.put("month", date.getMonth());
            map.put("userIsOwner", date.getUser().getId().equals(user.getId()));
            maps.add(map);
        }
        return ResponseEntity.ok(maps);
    }
}
