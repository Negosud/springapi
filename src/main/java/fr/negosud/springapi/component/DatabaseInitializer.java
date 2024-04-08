package fr.negosud.springapi.component;

import fr.negosud.springapi.service.*;
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
    final private ProductTransactionTypeService productTransactionTypeService;
    final private ProductService productService;

    @Autowired
    public DatabaseInitializer(
            PermissionNodeService permissionNodeService,
            UserGroupService userGroupService,
            UserService userService,
            ProductFamilyService productFamilyService,
            ProductTransactionTypeService productTransactionTypeService,
            ProductService productService) {
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
