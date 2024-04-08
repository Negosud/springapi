package fr.negosud.springapi.repository;

import fr.negosud.springapi.model.entity.PermissionNode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionNodeRepository extends JpaRepository<PermissionNode, Long> {

    Optional<PermissionNode> findByParentPermissionNodeIsNullAndName(String name);
    Optional<PermissionNode> findByParentPermissionNodeAndName(PermissionNode parentPermissionNode, String name);

}
