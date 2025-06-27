package com.vandan.codereview.service;

import com.vandan.codereview.dto.ReviewTrendDto;
import com.vandan.codereview.model.PullRequest;
import com.vandan.codereview.model.ReviewFeedback;
import com.vandan.codereview.repository.PullRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {


    @Autowired
    private PullRequestRepository pullRequestRepository;

    public Map<String, Object> getDashboardStats() {
        List<PullRequest> prs = pullRequestRepository.findAllWithFeedbacks();

        int totalPRs = prs.size();

        double avgReviewTime = prs.stream()
                .filter(pr -> pr.getCreatedAt() != null && !pr.getFeedbacks().isEmpty())
                .mapToLong(pr -> {
                    LocalDateTime reviewedAt = getLatestFeedbackTime(pr);
                    return Duration.between(pr.getCreatedAt(), reviewedAt).toMillis();
                })
                .average()
                .orElse(0.0) / 60.0;

        long totalFeedbacks = prs.stream()
                .flatMap(pr -> pr.getFeedbacks().stream())
                .count();

        long accurateFeedbacks = prs.stream()
                .flatMap(pr -> pr.getFeedbacks().stream())
                .filter(fb -> Boolean.TRUE.equals(fb.getIsAccurate()))
                .count();
        double accuracy = totalFeedbacks == 0 ? 0.0 : (accurateFeedbacks * 100.0) / totalFeedbacks;

        Map<String, Object> response = new HashMap<>();
        response.put("totalPRs", totalPRs);
        response.put("avgReviewTime", String.format("%.2f", avgReviewTime) + " sec");
        response.put("reviewAccuracy", String.format("%.1f", accuracy) + "%");

        return response;
    }

    private LocalDateTime getLatestFeedbackTime(PullRequest pr) {
        return pr.getFeedbacks().stream()
                .map(ReviewFeedback::getCreatedAt)
                .max(Comparator.naturalOrder())
                .orElse(null);
    }

    public List<ReviewTrendDto> getReviewTrends() {
        return pullRequestRepository.getReviewTrends();
    }
}
