package org.example;

import org.example.service.PropertyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AccessDeniedController {
    private PropertyService propertyService;
    public AccessDeniedController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }
    @RequestMapping("/accessDenied")
    public String accessDenied() {
        return "accessDenied";
    }
}
