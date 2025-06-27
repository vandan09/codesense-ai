package com.vandan.codereview.repository;

import com.vandan.codereview.dto.ReviewTrendDto;
import com.vandan.codereview.model.PullRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PullRequestRepository extends JpaRepository<PullRequest, Long> {
    @Query("SELECT pr FROM PullRequest pr JOIN FETCH pr.feedbacks")
    List<PullRequest> findAllWithFeedbacks();


    @Query("SELECT new com.vandan.codereview.dto.ReviewTrendDto(CAST(pr.createdAt AS LocalDate), COUNT(pr)) " +
            "FROM PullRequest pr " +
            "GROUP BY CAST(pr.createdAt AS LocalDate) " +
            "ORDER BY CAST(pr.createdAt AS LocalDate)")
    List<ReviewTrendDto> getReviewTrends();

}

