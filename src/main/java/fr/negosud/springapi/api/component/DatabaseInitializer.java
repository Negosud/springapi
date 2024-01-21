package fr.negosud.springapi.api.component;

import fr.negosud.springapi.api.service.PermissionNodeService;
import fr.negosud.springapi.api.service.ProductFamilyService;
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
    final private ProductFamilyService productFamilyService;

    @Autowired
    public DatabaseInitializer(PermissionNodeService permissionNodeService, UserGroupService userGroupService, UserService userService, ProductFamilyService productFamilyService) {
        this.permissionNodeService = permissionNodeService;
        this.userGroupService = userGroupService;
        this.userService = userService;
        this.productFamilyService = productFamilyService;
    }

    public void run(ApplicationArguments args) {
        if (!this.permissionNodeService.initPermissionNodes())
            throw new RuntimeException("Permission nodes initialization failed");

        if (!this.userGroupService.initUserGroups())
            throw new RuntimeException("User groups initialization failed");

        if (!this.userService.initUsers())
            throw new RuntimeException("Users initialization failed");

        if (!this.productFamilyService.initProductFamilies())
            throw new RuntimeException("Product families initialization failed");
    }
}
