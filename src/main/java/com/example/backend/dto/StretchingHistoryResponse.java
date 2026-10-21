package com.example.backend.dto;

import com.example.backend.domain.StretchingHistory;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "스트레칭 수행 기록 응답")
public class StretchingHistoryResponse {

    @Schema(description = "스트레칭 기록 ID", example = "12")
    private Long id;

    @Schema(description = "수행한 스트레칭 이름",
            example = "스트레칭 이름")
    private String stretchingName;

    @Schema(description = "실제 수행 시간(초)", example = "60")
    private Integer durationSeconds;

    @Schema(description = "스트레칭 완료 시각 (한국 시간 기준)",
            type = "string",
            format = "date-time",
            example = "2026-10-22T08:30:00")
    private LocalDateTime completedAt;

    public StretchingHistoryResponse(StretchingHistory history) {
        this.id = history.getId();
        this.stretchingName = history.getStretchingName();
        this.durationSeconds = history.getDurationSeconds();
        this.completedAt = history.getCompletedAt();
    }

    public Long getId() {
        return id;
    }

    public String getStretchingName() {
        return stretchingName;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
}
