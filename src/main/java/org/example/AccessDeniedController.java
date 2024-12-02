package org.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AccessDeniedController {
    @RequestMapping("/accessDenied")
    public String accessDenied() {
        return "accessDenied";
    }
}
