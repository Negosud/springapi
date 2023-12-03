package fr.negosud.springapi.api.component;

import fr.negosud.springapi.api.service.PermissionNodeService;
import fr.negosud.springapi.api.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements ApplicationRunner {

    final private PermissionNodeService permissionNodeService;
    final private UserGroupService userGroupService;

    @Autowired
    public DatabaseInitializer(PermissionNodeService permissionNodeService, UserGroupService userGroupService) {
        this.permissionNodeService = permissionNodeService;
        this.userGroupService = userGroupService;
    }

    public void run(ApplicationArguments args) {
        if (!this.permissionNodeService.initPermissionNodes()) {
            throw new RuntimeException("Permission nodes initialization failed");
        } else {
            if (!this.userGroupService.initUserGroups()) {
                throw new RuntimeException("User groups initialization failed");
            } else {
                if (!this.userGroupService.fillUserGroupsPermissions()) {
                    throw new RuntimeException("Permissions attribution failed");
                }
            }
        }
    }
}
