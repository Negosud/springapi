package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.entity.PermissionNode;
import fr.negosud.springapi.api.repository.PermissionNodeRepository;
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
final public class PermissionNodeService {

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

    public Optional<PermissionNode> getPermissionNodeByFullName(String fullName) {
        String[] nameParts = fullName.split("\\.");
        PermissionNode currentNode = null;
        for (String namePart : nameParts) {
            if (currentNode == null) {
                currentNode = permissionNodeRepository.findByParentPermissionNodeIsNullAndName(namePart).orElse(null);
            } else {
                currentNode = permissionNodeRepository.findByParentPermissionNodeAndName(currentNode, namePart).orElse(null);
            }

            if (currentNode == null) {
                break;
            }
        }

        return Optional.ofNullable(currentNode);
    }

    public boolean initPermissionNodes() {
        Yaml yaml = new Yaml();

        try (InputStream inputStream = new ClassPathResource("permissions.yml").getInputStream()) {
            Map<String, Object> permissionsMap = yaml.load(inputStream);
            if (permissionsMap != null && permissionsMap.containsKey("permissions")) {
                List<Map<String, Object>> permissionsList = (List<Map<String, Object>>) permissionsMap.get("permissions");
                for (Map<String, Object> permission : permissionsList) {
                    buildPermissionTree(null, permission);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void buildPermissionTree(PermissionNode parentNode, Map<String, Object> permissionMap) {
        String permissionName = (String) permissionMap.get("name");

        Optional<PermissionNode> permissionNodeOptional = permissionNodeRepository.findByParentPermissionNodeAndName(parentNode, permissionName);

        PermissionNode permissionNode;
        permissionNode = permissionNodeOptional.orElseGet(() -> savePermissionNode(new PermissionNode(permissionName, parentNode)));

        List<Map<String, Object>> childrenList = (List<Map<String, Object>>) permissionMap.get("children");
        if (childrenList != null) {
            for (Map<String, Object> childMap : childrenList) {
                buildPermissionTree(permissionNode, childMap);
            }
        }
    }
}
