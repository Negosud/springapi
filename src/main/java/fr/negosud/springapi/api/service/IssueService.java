package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.entity.Issue;
import fr.negosud.springapi.api.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IssueService {

    private final IssueRepository issueRepository;

    @Autowired
    public IssueService(IssueRepository issueRepository) {

        this.issueRepository = issueRepository;
    }

    public List<Issue> getAllIssues() {

        return issueRepository.findAll();
    }

    public Optional<Issue> getIssueById(Long issueId) {

        return issueRepository.findById(issueId);
    }

    public Issue saveIssue(Issue issue) {

        return issueRepository.save(issue);
    }

    public void deleteIssue(Long issueId) {

        issueRepository.deleteById(issueId);
    }
}
