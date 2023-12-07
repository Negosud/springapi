package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.model.entity.PermissionNode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionNodeRepository extends JpaRepository<PermissionNode, Long> {

    Optional<PermissionNode> findByNameAndParentPermissionNodeIsNull(String name);
    Optional<PermissionNode> findByNameAndParentPermissionNode(String name, PermissionNode parentPermissionNode);

}
