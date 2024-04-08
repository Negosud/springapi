package fr.negosud.springapi.service;

import fr.negosud.springapi.model.entity.Issue;
import fr.negosud.springapi.repository.IssueRepository;
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

    public Optional<Issue> getIssueById(long issueId) {
        return issueRepository.findById(issueId);
    }

    public Issue saveIssue(Issue issue) {
        return issueRepository.save(issue);
    }

    public void deleteIssue(long issueId) {
        issueRepository.deleteById(issueId);
    }
}
