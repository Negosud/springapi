package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String email);

    List<User> findAllByActiveAndUserGroupName(boolean active, String userGroupName);

    List<User> findAllByActive(boolean active);
}
