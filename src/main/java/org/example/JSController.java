package org.example;

import org.example.model.BookingDates;
import org.example.model.Dates;
import org.example.model.Property;
import org.example.model.User;
import org.example.repos.DateRepository;
import org.example.service.DateService;
import org.example.repos.PropertyRepository;
import org.example.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class JSController {
    @Autowired
    private final DateService dateService;
    @Autowired
    private final DateRepository dateRepository;
    @Autowired
    private final PropertyRepository propertyRepository;
    @Autowired
    private final UserRepository userRepository;
    public JSController(DateService dateService, DateRepository dateRepository, PropertyRepository propertyRepository, UserRepository userRepository) {
        this.dateService = dateService;
        this.dateRepository = dateRepository;
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
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

    @GetMapping("/coord")
    public ResponseEntity<List<Map<String, Object>>> TransitCoords(Model model) {
        List<Property> properties = propertyRepository.findAll();

        List<Map<String, Object>> maps = new ArrayList<>();
        for (Property property: properties) {
            Map<String, Object> map = new HashMap<>();
            map.put("latitude", property.getLatitude());
            map.put("longitude", property.getLongitude());
            map.put("name", property.getName());
            map.put("id", property.getId());
            maps.add(map);
        }
        return ResponseEntity.ok(maps);
    }

    @PostMapping("/book-dates")
    public ResponseEntity<Map<String, String>> postBookDates(
            @RequestBody List<BookingDates> bookingDates,
            Authentication authentication, Model model) {
        for (BookingDates date: bookingDates) {
            System.out.println("Date: " + date.getYear() + "-" + date.getMonth() + "-" + date.getDay());
            System.out.println("Property ID: " + date.getPropertyId());
        }
        User user = new User();
        try {
            user = userRepository.findByName(
                authentication.getName()
            ).get();
        }
        catch (IllegalArgumentException e) {
            model.addAttribute("error", "пользователь не авторизован");
        }
        dateService.addDates(bookingDates, user);
        Map<String, String> response = new HashMap<>();
        response.put("messege", "Даты добавленны");
        return ResponseEntity.ok(response);
    }
}
