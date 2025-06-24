package com.vandan.codereview.controller;
import com.vandan.codereview.dto.PullRequestDto;
import com.vandan.codereview.dto.ReviewFeedbackDto;
import com.vandan.codereview.model.PullRequest;
import com.vandan.codereview.repository.PullRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private PullRequestRepository pullRequestRepository;

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
                return fbd;
            }).collect(Collectors.toList());

            dto.setFeedbacks(feedbackDtos);
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}

