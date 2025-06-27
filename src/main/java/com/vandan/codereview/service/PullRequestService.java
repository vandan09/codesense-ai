package com.vandan.codereview.service;

import com.vandan.codereview.model.PullRequest;
import com.vandan.codereview.model.ReviewFeedback;
import com.vandan.codereview.repository.PullRequestRepository;
import com.vandan.codereview.repository.ReviewFeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PullRequestService {

    @Autowired
    private PullRequestRepository pullRequestRepository;

    @Autowired
    private ReviewFeedbackRepository reviewFeedbackRepository;

    public void saveReviewData(String repo, int prNumber, String action, String diffUrl, String diffContent, String feedbackText, String severity, boolean commented) {
        PullRequest pr = new PullRequest();
        pr.setRepoName(repo);
        pr.setPrNumber(prNumber);
        pr.setAction(action);
        pr.setDiffUrl(diffUrl);
        pullRequestRepository.save(pr);

        ReviewFeedback review = new ReviewFeedback();
        review.setPullRequest(pr);
        review.setDiffContent(diffContent);
        review.setFeedback(feedbackText);
        review.setCommentedOnGitHub(commented);
        review.setSeverity(severity);
        review.setIsAccurate(true);
        reviewFeedbackRepository.save(review);

    }
}
