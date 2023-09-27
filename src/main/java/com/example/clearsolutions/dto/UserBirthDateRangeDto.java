package com.example.clearsolutions.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class UserBirthDateRangeDto {
    @Valid
    @NotNull
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate startDate;
    @Valid
    @NotNull
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate endDate;

    public UserBirthDateRangeDto() {
    }

    public UserBirthDateRangeDto(@NotNull LocalDate startDate, @NotNull LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
