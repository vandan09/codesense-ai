package com.vandan.codereview.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PullRequestDto {
    private Long id;
    private String repoName;
    private Integer prNumber;
    private String diffUrl;
    private String action;
    private LocalDateTime createdAt;
    private List<ReviewFeedbackDto> feedbacks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public Integer getPrNumber() {
        return prNumber;
    }

    public void setPrNumber(Integer prNumber) {
        this.prNumber = prNumber;
    }

    public String getDiffUrl() {
        return diffUrl;
    }

    public void setDiffUrl(String diffUrl) {
        this.diffUrl = diffUrl;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<ReviewFeedbackDto> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<ReviewFeedbackDto> feedbacks) {
        this.feedbacks = feedbacks;
    }
}

