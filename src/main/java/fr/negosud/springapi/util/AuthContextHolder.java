package fr.negosud.springapi.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class AuthContextHolder {
    private static final ThreadLocal<String> callingApplicationRoleHolder = new ThreadLocal<>();

    public static String getCallingApplicationRole() {
        if (callingApplicationRoleHolder.get() == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String role = authentication.getAuthorities().iterator().next().getAuthority().substring(5);
            callingApplicationRoleHolder.set(role);
        }
        return callingApplicationRoleHolder.get();
    }
}
