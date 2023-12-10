package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.model.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    Optional<UserGroup> findByName(String name);

}
