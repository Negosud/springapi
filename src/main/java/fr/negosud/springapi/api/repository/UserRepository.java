package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
