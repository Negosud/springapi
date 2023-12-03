package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.entity.PermissionNode;
import fr.negosud.springapi.api.repository.PermissionNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionNodeService {

    final private PermissionNodeRepository permissionNodeRepository;

    @Autowired
    public PermissionNodeService(PermissionNodeRepository permissionNodeRepository) {
        this.permissionNodeRepository = permissionNodeRepository;
    }

    public List<PermissionNode> getAllPermissionNodes() {
        return permissionNodeRepository.findAll();
    }

    public Optional<PermissionNode> getPermissionNodeById(Long permissionNodeId) {
        return permissionNodeRepository.findById(permissionNodeId);
    }

    public PermissionNode savePermissionNode(PermissionNode permissionNode) {
        return permissionNodeRepository.save(permissionNode);
    }

    public void deletePermissionNode(Long permissionNodeId) {
        permissionNodeRepository.deleteById(permissionNodeId);
    }

    public boolean initPermissionNodes() {
        return true;
    }
}
