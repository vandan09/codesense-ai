package com.vandan.codereview.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ReviewFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pull_request_id")
    private PullRequest pullRequest;

    @Column(columnDefinition = "TEXT")
    private String diffContent;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    private boolean commentedOnGitHub;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private Boolean isAccurate;

    @Column(length = 20)
    private String severity;

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Boolean getIsAccurate() {
        return isAccurate;
    }

    public void setIsAccurate(Boolean isAccurate) {
        this.isAccurate = isAccurate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PullRequest getPullRequest() {
        return pullRequest;
    }

    public void setPullRequest(PullRequest pullRequest) {
        this.pullRequest = pullRequest;
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
}
