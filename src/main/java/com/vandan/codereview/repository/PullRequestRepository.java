package com.vandan.codereview.repository;

import com.vandan.codereview.model.PullRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PullRequestRepository extends JpaRepository<PullRequest, Long> {
    @Query("SELECT pr FROM PullRequest pr JOIN FETCH pr.feedbacks")
    List<PullRequest> findAllWithFeedbacks();
}