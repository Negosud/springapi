package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.entity.PermissionNode;
import fr.negosud.springapi.api.model.entity.UserGroup;
import fr.negosud.springapi.api.repository.UserGroupRepository;
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
public class UserGroupService {

    private final UserGroupRepository userGroupRepository;
    private final PermissionNodeService permissionNodeService;

    @Autowired
    public UserGroupService(UserGroupRepository userGroupRepository, PermissionNodeService permissionNodeService) {
        this.userGroupRepository = userGroupRepository;
        this.permissionNodeService = permissionNodeService;
    }

    public List<UserGroup> getAllUserGroups() {
        return userGroupRepository.findAll();
    }

    public Optional<UserGroup> getUserGroupById(Long userGroupId) {
        return userGroupRepository.findById(userGroupId);
    }

    public void saveUserGroup(UserGroup userGroup) {
        userGroupRepository.save(userGroup);
    }

    public void deleteUserGroup(Long userGroupId) {
        userGroupRepository.deleteById(userGroupId);
    }

    public boolean initUserGroups() {
        Yaml yaml = new Yaml();

        try (InputStream inputStream = new ClassPathResource("userGroups.yml").getInputStream()) {
            Map<String, List<Map<String, Object>>> userGroupsMap = yaml.load(inputStream);
            if (userGroupsMap != null && userGroupsMap.containsKey("userGroups")) {
                List<Map<String, Object>> userGroupsList = userGroupsMap.get("userGroups");
                for (Map<String, Object> userGroupInfo : userGroupsList) {
                    String name = (String) userGroupInfo.get("name");
                    UserGroup userGroup = this.userGroupRepository.findByName(name).orElse(null);
                    if (userGroup == null) {
                        UserGroup childUserGroup = this.userGroupRepository.findByName((String) userGroupInfo.get("inherit")).orElse(null);
                        List<PermissionNode> permissionNodeList = this.permissionNodeService.getPermissionNodeListByFullName((List<String>) userGroupInfo.get("permissions")).orElse(null);
                        this.saveUserGroup(new UserGroup(name, childUserGroup, permissionNodeList));
                    } else {
                        System.out.println("Mise à jour des permissions pour le groupe " + userGroup);
                        userGroup.addPermissionNodeList(this.permissionNodeService.getPermissionNodeListByFullName((List<String>) userGroupInfo.get("permissions")).orElse(null));
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
