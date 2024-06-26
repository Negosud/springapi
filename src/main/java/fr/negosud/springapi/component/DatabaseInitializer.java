package fr.negosud.springapi.component;

import fr.negosud.springapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements ApplicationRunner {

    private final PermissionNodeService permissionNodeService;
    private final UserGroupService userGroupService;
    private final UserService userService;
    private final ProductFamilyService productFamilyService;
    private final ProductTransactionTypeService productTransactionTypeService;
    private final ProductService productService;

    @Autowired
    public DatabaseInitializer(
            @Lazy PermissionNodeService permissionNodeService,
            @Lazy UserGroupService userGroupService,
            @Lazy UserService userService,
            @Lazy ProductFamilyService productFamilyService,
            @Lazy ProductTransactionTypeService productTransactionTypeService,
            @Lazy ProductService productService) {
        this.permissionNodeService = permissionNodeService;
        this.userGroupService = userGroupService;
        this.userService = userService;
        this.productFamilyService = productFamilyService;
        this.productTransactionTypeService = productTransactionTypeService;
        this.productService = productService;
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

        if (!this.productTransactionTypeService.initProductTransactionTypes())
            throw new RuntimeException("Product transaction types initialization failed");

        if (!this.productService.initProducts())
            throw new RuntimeException("Products initialization failed");
    }
}
