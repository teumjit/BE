package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "스트레칭 수행 기록 요청")
public class StretchingHistoryRequest {

    @NotBlank
    @Size(max = 100)
    @Schema(description = "수행한 스트레칭 이름",
            example = "목 스트레칭",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String stretchingName;

    @NotNull
    @Min(1)
    @Max(3600)
    @Schema(description = "실제 수행 시간(초)",
            example = "60",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer durationSeconds;

    public StretchingHistoryRequest() {
    }

    public String getStretchingName() {
        return stretchingName;
    }

    public void setStretchingName(String stretchingName) {
        this.stretchingName = stretchingName;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
}