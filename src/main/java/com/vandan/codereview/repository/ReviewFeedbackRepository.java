package com.vandan.codereview.repository;

import com.vandan.codereview.model.ReviewFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewFeedbackRepository extends JpaRepository<ReviewFeedback, Long> {}
