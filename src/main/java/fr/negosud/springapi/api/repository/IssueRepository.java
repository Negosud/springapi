package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.model.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {

}
