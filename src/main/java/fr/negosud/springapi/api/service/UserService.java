package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.entity.User;
import fr.negosud.springapi.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
final public class UserService {

    private final UserRepository userRepository;
    private final PermissionNodeService permissionNodeService;
    private final UserGroupService userGroupService;

    @Autowired
    public UserService(UserRepository userRepository, PermissionNodeService permissionNodeService, UserGroupService userGroupService) {
        this.userRepository = userRepository;
        this.permissionNodeService = permissionNodeService;
        this.userGroupService = userGroupService;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByPseudoUniqueKey(String pseudoUniqueKey) {
        return Optional.ofNullable(this.userRepository.findByLogin(pseudoUniqueKey).orElse(this.userRepository.findByEmail(pseudoUniqueKey).orElse(null)));
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    public boolean initUsers() {
        Yaml yaml = new Yaml();

        try (InputStream inputStream = new ClassPathResource("users.yml").getInputStream()) {
            Map<String, List<Map<String, Object>>> usersMap = yaml.load(inputStream);
            if (usersMap != null && usersMap.containsKey("users")) {
                List<Map<String, Object>> usersList = usersMap.get("users");
                for (Map<String, Object> userInfo : usersList) {
                    String pseudoUniqueKey = (String) userInfo.get("pseudouniquekey");
                    System.out.println("Trying " + pseudoUniqueKey);
                    User existingUser = this.getUserByPseudoUniqueKey(pseudoUniqueKey).orElse(null);
                    if (existingUser == null) {
                        User user = new User(
                                userInfo.containsKey("password") ? pseudoUniqueKey : null,
                                (String) userInfo.get("password"),
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
