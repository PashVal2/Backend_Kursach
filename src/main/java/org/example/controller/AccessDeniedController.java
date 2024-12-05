package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AccessDeniedController {
    @RequestMapping("/accessDenied") // GET-запрос для страницы при отказе доступа при переходе на страницу добавления недвижимости
    public String accessDenied() {
        return "accessDenied";
    }
}
