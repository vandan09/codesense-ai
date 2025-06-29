package com.vandan.codereview.controller;
import com.vandan.codereview.dto.PullRequestDto;
import com.vandan.codereview.dto.ReviewFeedbackDto;
import com.vandan.codereview.dto.ReviewTrendDto;
import com.vandan.codereview.model.PullRequest;
import com.vandan.codereview.model.ReviewFeedback;
import com.vandan.codereview.repository.PullRequestRepository;
import com.vandan.codereview.repository.ReviewFeedbackRepository;
import com.vandan.codereview.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard APIs", description = "APIs for visualizing review and feedback analytics")
public class DashboardController {

    @Autowired
    private PullRequestRepository pullRequestRepository;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private ReviewFeedbackRepository reviewFeedbackRepository;

    @Operation(
            summary = "Get list of all reviews",
            description = "Fetches a list of all pull requests and their associated structured feedback. Useful for rendering the reviews list page."
    )
    @GetMapping("/reviews")
    public ResponseEntity<List<PullRequestDto>> getAllReviews() {
        List<PullRequest> prs = pullRequestRepository.findAllWithFeedbacks();

        List<PullRequestDto> response = prs.stream().map(pr -> {
            PullRequestDto dto = new PullRequestDto();
            dto.setId(pr.getId());
            dto.setRepoName(pr.getRepoName());
            dto.setPrNumber(pr.getPrNumber());
            dto.setDiffUrl(pr.getDiffUrl());
            dto.setAction(pr.getAction());
            dto.setCreatedAt(pr.getCreatedAt());

            List<ReviewFeedbackDto> feedbackDtos = pr.getFeedbacks().stream().map(fb -> {
                ReviewFeedbackDto fbd = new ReviewFeedbackDto();
                fbd.setId(fb.getId());
                fbd.setCommentedOnGitHub(fb.isCommentedOnGitHub());
                fbd.setCreatedAt(fb.getCreatedAt());
                fbd.setDiffContent(fb.getDiffContent());
                fbd.setFeedback(fb.getFeedback());
                fbd.setSeverity(fb.getSeverity());
                return fbd;
            }).collect(Collectors.toList());

            dto.setFeedbacks(feedbackDtos);
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Get overall dashboard statistics",
            description = "Returns total number of pull requests, total feedback count, and other aggregated metrics for the dashboard top stats section."
    )
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(dashboardService.getDashboardStats());
    }

    @Operation(
            summary = "Get review trend data",
            description = "Returns data points showing the trend of pull request reviews over time, useful for line/bar charts in the dashboard."
    )
    @GetMapping("/review-trends")
    public ResponseEntity<List<ReviewTrendDto>> getReviewTrends() {
        return ResponseEntity.ok(dashboardService.getReviewTrends());
    }

    @Operation(
            summary = "Get feedback summary by severity",
            description = "Returns a breakdown of feedback based on severity or category (e.g., Code Style, Performance, Bug Risk) for chart display."
    )
    @GetMapping("/severity-summary")
    public Map<String, Long> getSeveritySummary() {
        List<ReviewFeedback> allFeedbacks = reviewFeedbackRepository.findAll();

        long critical = allFeedbacks.stream()
                .filter(fb -> "Critical".equalsIgnoreCase(fb.getSeverity()))
                .count();
        long medium = allFeedbacks.stream()
                .filter(fb -> "Medium".equalsIgnoreCase(fb.getSeverity()))
                .count();
        long normal = allFeedbacks.stream()
                .filter(fb -> "Normal".equalsIgnoreCase(fb.getSeverity()))
                .count();

        return Map.of(
                "critical", critical,
                "medium", medium,
                "normal", normal
        );
    }


}

