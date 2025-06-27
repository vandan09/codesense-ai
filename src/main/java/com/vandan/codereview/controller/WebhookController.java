package com.vandan.codereview.controller;
import com.vandan.codereview.service.GeminiService;
import com.vandan.codereview.service.GitHubService;
import com.vandan.codereview.service.PullRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @Autowired
    private GitHubService gitHubService;

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private PullRequestService pullRequestService;


    @Value("${github.token}")
    private String githubToken;

    @PostMapping("/github")
    public ResponseEntity<Void> handleGitHubWebhook(@RequestBody Map<String, Object> payload) {
        System.out.println("Webhook triggered successfully");

        String action = (String) payload.get("action");
        Integer prNumber = (Integer) payload.get("number");
        Map<String, Object> repository = (Map<String, Object>) payload.get("repository");
        Map<String, Object> owner = (Map<String, Object>) repository.get("owner");
        Map<String, Object> pullRequest = (Map<String, Object>) payload.get("pull_request");

        if (owner == null || pullRequest == null) {
            return ResponseEntity.badRequest().build();
        }

        String repoName = (String) repository.get("name");
        String ownerLogin = (String) owner.get("login");
        String diffUrl = (String) pullRequest.get("diff_url");

        System.out.printf("Action: %s\nPR Number: %d\nRepo: %s/%s\nDiff URL: %s%n", action, prNumber, ownerLogin, repoName, diffUrl);

        if (githubToken == null || githubToken.isEmpty()) {
            return ResponseEntity.internalServerError().build();
        }

        String diff = gitHubService.fetchDiff(diffUrl, githubToken);

        if (diff == null || diff.startsWith("ERROR") || diff.trim().isEmpty()) {
            System.out.println("Failed to fetch diff content");
        } else {
            System.out.printf("Diff fetched successfully (%d characters)\n", diff.length());
        }

        if (diff != null && !diff.startsWith("ERROR")) {
            String geminiFeedback = geminiService.getReviewFromGemini(diff);
            System.out.println("Gemini Feedback:");
            System.out.println(geminiFeedback);
            gitHubService.postCommentToPullRequest(ownerLogin, repoName, prNumber, geminiFeedback, githubToken);

            String[] lines = geminiFeedback.split("\n", 2);
            String severityLine = lines[0].trim();
            String severity;
            if (severityLine.toLowerCase().contains("critical")) {
                severity = "Critical";
            } else if (severityLine.toLowerCase().contains("medium")) {
                severity = "Medium";
            } else {
                severity = "Normal";
            }


            pullRequestService.saveReviewData(
                    ownerLogin + "/" + repoName,
                    prNumber,
                    action,
                    diffUrl,
                    diff,
                    geminiFeedback,
                    severity,
                    true
            );
        }
        return ResponseEntity.ok().build();
    }
}
