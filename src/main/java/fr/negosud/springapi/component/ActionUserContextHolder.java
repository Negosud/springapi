package fr.negosud.springapi.component;

import fr.negosud.springapi.model.entity.User;
import fr.negosud.springapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ActionUserContextHolder {
    private static final ThreadLocal<User> userIdHolder = new ThreadLocal<>();
    private final UserService userService;

    @Autowired
    public ActionUserContextHolder(@Lazy UserService userService) {
        this.userService = userService;
    }

    public static User getActionUser() {
        return userIdHolder.get();
    }

    public void setActionUserId(long userId) {
        if (userId != 0)
            this.userService.getUserById(userId).ifPresent(userIdHolder::set);
    }

    public static boolean can(String permission) {
        return userIdHolder.get() != null && userIdHolder.get().can(permission);
    }
}
