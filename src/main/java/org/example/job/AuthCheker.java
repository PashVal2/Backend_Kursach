package org.example.job;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthCheker {
    public static boolean isAuth(Authentication authentication) { // проверка входа в акк
        if (authentication != null &&
                authentication.isAuthenticated()) {
            return true;
        }
        else {
            return false;
        }
    }
    public static boolean isAdmin(Authentication authentication) { // проверка на админа
        if (authentication != null &&
                !authentication.getAuthorities().isEmpty()) {
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            System.out.println("Role: " + role);
            if (role.equals("ROLE_ADMIN")) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }
}
