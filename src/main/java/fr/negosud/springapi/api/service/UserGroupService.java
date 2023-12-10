package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.entity.UserGroup;
import fr.negosud.springapi.api.repository.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
final public class UserGroupService {

    private final UserGroupRepository userGroupRepository;

    @Autowired
    public UserGroupService(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    public List<UserGroup> getAllUserGroups() {
        return userGroupRepository.findAll();
    }

    public Optional<UserGroup> getUserGroupById(Long userGroupId) {
        return userGroupRepository.findById(userGroupId);
    }

    public UserGroup saveUserGroup(UserGroup userGroup) {
        return userGroupRepository.save(userGroup);
    }

    public void deleteUserGroup(Long userGroupId) {
        userGroupRepository.deleteById(userGroupId);
    }

    public boolean initUserGroups() {


        return true;
    }

    public boolean fillUserGroupsPermissions() {
        return true;
    }

}
