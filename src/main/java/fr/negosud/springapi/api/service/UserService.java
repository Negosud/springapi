package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.component.UserPasswordEncoder;
import fr.negosud.springapi.api.model.dto.request.SetUserRequest;
import fr.negosud.springapi.api.model.entity.SupplierProduct;
import fr.negosud.springapi.api.model.entity.User;
import fr.negosud.springapi.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.System.out;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PermissionNodeService permissionNodeService;
    private final UserGroupService userGroupService;
    private final AddressService addressService;
    private final SupplierProductService supplierProductService;

    @Autowired
    public UserService(UserRepository userRepository, PermissionNodeService permissionNodeService, UserGroupService userGroupService, AddressService addressService, SupplierProductService supplierProductService) {
        this.userRepository = userRepository;
        this.permissionNodeService = permissionNodeService;
        this.userGroupService = userGroupService;
        this.addressService = addressService;
        this.supplierProductService = supplierProductService;
    }

    public List<User> getAllUsers(Optional<Boolean> active, String userGroupName) {
        return (userGroupName == null) ?
                (active.isEmpty() ?
                        userRepository.findAll() :
                        userRepository.findAllByActive(active.get())) :
                (active.isEmpty() ?
                        userRepository.findAllByUserGroupName(userGroupName) :
                        userRepository.findAllByActiveAndUserGroupName(active.get(), userGroupName));
    }

    public Optional<User> getUserById(long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByPseudoUniqueKey(String pseudoUniqueKey) {
        return Optional.ofNullable(this.userRepository.findByLogin(pseudoUniqueKey).orElse(this.userRepository.findByEmail(pseudoUniqueKey).orElse(null)));
    }

    public void saveUser(User user) {
        if (user.getUserGroup() != null && user.getUserGroup().getName().equals("SUPPLIER") && user.getSuppliedProductList() != null && !user.getSuppliedProductList().isEmpty()) {
            List<SupplierProduct> supplierProductList = new ArrayList<>(user.getSuppliedProductList());
            user.setSuppliedProductList(null);
            userRepository.save(user);
            user.setSuppliedProductList(supplierProductList);
            out.println("SupplierProduct List :");
            out.println(supplierProductList);
            for (SupplierProduct supplierProduct : supplierProductList) {
                if (supplierProduct != null)
                    supplierProductService.saveSupplierProduct(supplierProduct);
            }
        }
        userRepository.save(user);
    }

    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    public User setUserFromRequest(SetUserRequest setUserRequest, User user) {
        if (user == null)
            user = new User();

        user.setEmail(setUserRequest.getEmail());
        user.setFirstName(setUserRequest.getFirstName());
        user.setLastName(setUserRequest.getLastName());
        user.setUserGroup(userGroupService.getUserGroupByName(setUserRequest.getUserGroup()).orElse(null));
        user.setActive(setUserRequest.isActive());
        user.setLogin(setUserRequest.getLogin());
        user.setPhoneNumber(setUserRequest.getPhoneNumber());
        user.setPermissionNodeList(permissionNodeService.getPermissionNodeListByFullName(setUserRequest.getPermissionList()).orElse(null));
        user.setMailingAddress(addressService.getAddressById(setUserRequest.getMailingAddress()).orElse(null));
        user.setBillingAddress(addressService.getAddressById(setUserRequest.getBillingAddress()).orElse(null));
        user.setSuppliedProductList(supplierProductService.setUsersSuppliedProductListFromRequest(user, setUserRequest.getSupplierProductList()));

        UserPasswordEncoder userPasswordEncoder = new UserPasswordEncoder();
        String userPassword = user.getPassword();
        if (setUserRequest.getPassword() != null && !(userPassword != null && (userPasswordEncoder.matchUserPassword(setUserRequest.getPassword(), userPassword)))) {
            user.setPassword(userPasswordEncoder.hashUserPassword(setUserRequest.getPassword()));
        }

        return user;
    }

    public boolean initUsers() {
        Yaml yaml = new Yaml();

        try (InputStream inputStream = new ClassPathResource("users.yml").getInputStream()) {
            Map<String, List<Map<String, Object>>> usersMap = yaml.load(inputStream);
            if (usersMap != null && usersMap.containsKey("users")) {
                List<Map<String, Object>> usersList = usersMap.get("users");
                for (Map<String, Object> userInfo : usersList) {
                    String pseudoUniqueKey = (String) userInfo.get("pseudouniquekey");
                    out.println("Trying " + pseudoUniqueKey);
                    User existingUser = this.getUserByPseudoUniqueKey(pseudoUniqueKey).orElse(null);
                    if (existingUser == null) {
                        UserPasswordEncoder userPasswordEncoder = new UserPasswordEncoder();
                        User user = new User(
                                userInfo.containsKey("password") ? pseudoUniqueKey : null,
                                userPasswordEncoder.hashUserPassword((String) userInfo.get("password")),
                                pseudoUniqueKey,
                                (String) userInfo.get("firstname"),
                                (String) userInfo.get("lastname"),
                                (String) userInfo.get("phonenumber"),
                                this.userGroupService.getUserGroupByName((String) userInfo.get("usergroup")).orElse(null),
                                this.permissionNodeService.getPermissionNodeListByFullName((List<String>) userInfo.get("permissions")).orElse(null)
                        );
                        this.saveUser(user);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
