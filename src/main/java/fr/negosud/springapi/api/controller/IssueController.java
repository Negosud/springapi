package fr.negosud.springapi.api.controller;

import fr.negosud.springapi.api.model.entity.Issue;
import fr.negosud.springapi.api.service.IssueService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/issue")
@Tag(name = "Issue")
public class IssueController {

    private final IssueService issueService;

    @Autowired
    public IssueController(IssueService issueService) {

        this.issueService = issueService;
    }

    @GetMapping
    public ResponseEntity<List<Issue>> getAllIssues() {

        List<Issue> issues = issueService.getAllIssues();
        return new ResponseEntity<>(issues, HttpStatus.OK);
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<Issue> getIssueById(@PathVariable Long issueId) {

        return issueService.getIssueById(issueId)
                .map(issue -> new ResponseEntity<>(issue, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Issue> createIssue(@RequestBody Issue issue) {

        Issue createdIssue= issueService.saveIssue(issue);
        return new ResponseEntity<>(createdIssue, HttpStatus.CREATED);
    }

    @PutMapping("/{issueId}/control")
    public ResponseEntity<Issue> updateIssue(@PathVariable Long issueId, @RequestBody Issue issue) {

        if (issueService.getIssueById(issueId).isPresent()) {
            issue.setIssueId(issueId);
            Issue updatedIssue = issueService.saveIssue(issue);
            return new ResponseEntity<>(updatedIssue, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{issueId}")
    public ResponseEntity<Void> deleteIssue(@PathVariable Long issueId) {

        if (issueService.getIssueById(issueId).isPresent()) {
            issueService.deleteIssue(issueId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}