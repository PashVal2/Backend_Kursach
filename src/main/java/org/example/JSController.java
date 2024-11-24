package org.example;

import org.example.model.BookingDates;
import org.example.model.User;
import org.example.repos.DateService;
import org.example.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class JSController {
    @Autowired
    private final DateService dateService;
    @Autowired
    private final UserRepository userRepository;
    public JSController(DateService dateService, UserRepository userRepository) {
        this.dateService = dateService;
        this.userRepository = userRepository;
    }
    @PostMapping("/book-dates")
    public ResponseEntity<Map<String, String>> postBookDates(
            @RequestBody List<BookingDates> bookingDates,
            Authentication authentication, Model model) {
        for (BookingDates date: bookingDates) {
            System.out.println("Date: " + date.getYear() + "-" + date.getMonth() + "-" + date.getDay());
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
