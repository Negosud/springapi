package fr.negosud.springapi.api.component;

import fr.negosud.springapi.api.model.entity.User;
import fr.negosud.springapi.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
final public class ActionUserContextHolder {
    private static final ThreadLocal<User> userIdHolder = new ThreadLocal<>();

    final private UserService userService;

    @Autowired
    public ActionUserContextHolder(UserService userService) {
        this.userService = userService;
    }

    public static User getActionUser() {
        return userIdHolder.get();
    }

    public void setActionUserId(Long userId) {
        userIdHolder.set(this.userService.getUserById(userId).orElse(null));
    }

    public static void clear() {
        userIdHolder.remove();
    }
}
