package com.vandan.codereview.dto;

import java.time.LocalDateTime;

public class ReviewFeedbackDto {
    private Long id;
    private boolean commentedOnGitHub;
    private LocalDateTime createdAt;
    private String diffContent;
    private String feedback;

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    private String severity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isCommentedOnGitHub() {
        return commentedOnGitHub;
    }

    public void setCommentedOnGitHub(boolean commentedOnGitHub) {
        this.commentedOnGitHub = commentedOnGitHub;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDiffContent() {
        return diffContent;
    }

    public void setDiffContent(String diffContent) {
        this.diffContent = diffContent;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}

