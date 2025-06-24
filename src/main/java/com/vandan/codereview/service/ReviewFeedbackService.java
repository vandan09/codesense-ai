package com.vandan.codereview.service;

import com.vandan.codereview.model.ReviewFeedback;
import com.vandan.codereview.repository.ReviewFeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ReviewFeedbackService {

    @Autowired
    ReviewFeedbackRepository reviewFeedbackRepository;

    public List<ReviewFeedback> getAllFeedbacks() {
        return reviewFeedbackRepository.findAll();
    }

}
