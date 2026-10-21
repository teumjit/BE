package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "오늘의 스트레칭 수행 요약")
public class TodayStretchingSummaryResponse {

    @Schema(description = "조회 기준 날짜", example = "2026-10-21")
    private final LocalDate date;

    @Schema(description = "총 수행 시간(초)", example = "135")
    private final long totalDurationSeconds;

    @Schema(description = "총 수행 시간(분, 소수점 버림)", example = "2")
    private final long totalDurationMinutes;

    @Schema(description = "완료 횟수", example = "3")
    private final long completedCount;

    public TodayStretchingSummaryResponse(
            LocalDate date,
            long totalDurationSeconds,
            long completedCount) {
        this.date = date;
        this.totalDurationSeconds = totalDurationSeconds;
        this.totalDurationMinutes = totalDurationSeconds / 60;
        this.completedCount = completedCount;
    }

    public LocalDate getDate() {
        return date;
    }

    public long getTotalDurationSeconds() {
        return totalDurationSeconds;
    }

    public long getTotalDurationMinutes() {
        return totalDurationMinutes;
    }

    public long getCompletedCount() {
        return completedCount;
    }
}