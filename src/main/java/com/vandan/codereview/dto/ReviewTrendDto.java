package com.vandan.codereview.dto;

import java.time.LocalDate;

public class ReviewTrendDto {
    private LocalDate date;
    private Long count;

    public ReviewTrendDto() {}

    public ReviewTrendDto(LocalDate date, Long count) {
        this.date = date;
        this.count = count;
    }

    // Getters and setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Long getCount() { return count; }
    public void setCount(Long count) { this.count = count; }
}
