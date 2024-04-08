package fr.negosud.springapi.repository;

import fr.negosud.springapi.model.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {

}
