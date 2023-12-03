package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

}
