package org.example.controller;

import org.example.model.*;
import org.example.service.DateService;
import org.example.service.NewsService;
import org.example.service.PropertyService;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class JSController {
    private final NewsService newsService;
    private final DateService dateService;
    private final PropertyService propertyService;
    private final UserService userService;
    public JSController(NewsService newsService, DateService dateService, PropertyService propertyService, UserService userService) {
        this.newsService = newsService;
        this.dateService = dateService;
        this.propertyService = propertyService;
        this.userService = userService;
    }
    @GetMapping("/book-dates/{propertyId}")
    public ResponseEntity<List<Map<String, Object>>> TransitDates(
        @PathVariable Long propertyId, Authentication authentication) {
        List<Dates> dates = dateService.findByPropertyId(propertyId);
        User user = userService.findByName(authentication.getName()).get();

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
    public ResponseEntity<List<Map<String, Object>>> getCoords(Model model) {
        List<Property> properties = propertyService.findAll();
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
    @GetMapping("/news")
    public ResponseEntity<List<Map<String, Object>>> postNews(Model model) {
        List<News> newsFromDb = newsService.getAllNews();
        List<Map<String, Object>> newsForPage = new ArrayList<>();

        for (News news: newsFromDb) {
            Map<String, Object> newsObj = new HashMap<>();
            // System.out.println(news.getTitle());
            newsObj.put("title", news.getTitle());
            newsObj.put("tag", news.getTag());
            newsObj.put("url", news.getUrl());
            newsForPage.add(newsObj);
        }
        return ResponseEntity.ok(newsForPage);
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
            user = userService.findByName(
                authentication.getName()
            ).get();
        }
        catch (IllegalArgumentException e) {
            model.addAttribute("error", "пользователь не авторизован");
        }
        dateService.addDates(bookingDates, user);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Даты добавленны");
        return ResponseEntity.ok(response);
    }
}
