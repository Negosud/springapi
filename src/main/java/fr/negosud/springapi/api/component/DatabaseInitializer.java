package fr.negosud.springapi.api.component;

import fr.negosud.springapi.api.service.PermissionNodeService;
import fr.negosud.springapi.api.service.UserGroupService;
import fr.negosud.springapi.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements ApplicationRunner {

    final private PermissionNodeService permissionNodeService;
    final private UserGroupService userGroupService;
    final private UserService userService;

    @Autowired
    public DatabaseInitializer(PermissionNodeService permissionNodeService, UserGroupService userGroupService, UserService userService) {
        this.permissionNodeService = permissionNodeService;
        this.userGroupService = userGroupService;
        this.userService = userService;
    }

    public void run(ApplicationArguments args) {
        if (!this.permissionNodeService.initPermissionNodes()) {
            throw new RuntimeException("Permission nodes initialization failed");
        } else {
            if (!this.userGroupService.initUserGroups()) {
                throw new RuntimeException("User groups initialization failed");
            } else {
                if (!this.userService.initUsers()) {
                    throw new RuntimeException("Users initialization failed");
                }
            }
        }
    }
}
