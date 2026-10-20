package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "스트레칭 난이도 및 제약 조건 등록·수정 요청")
public class StretchingDifficultyRequest {

    @Schema(
        description = "대상 스트레칭의 ID",
        example = "1"
    )
    private Long stretchingId;

    @Schema(
        description = "난이도 또는 제약 조건의 유형",
        example = "POSTURE:/CONSTRAINT:"
    )
    private String difficultyType;

    @Schema(
        description = "등록할 난이도 또는 제약 조건 값",
        example = "STANDING/ONE_HANDS"
    )
    private String difficulty;

    public StretchingDifficultyRequest() {
    }

    public Long getStretchingId() {
        return stretchingId;
    }

    public void setStretchingId(Long stretchingId) {
        this.stretchingId = stretchingId;
    }

    public String getDifficultyType() {
        return difficultyType;
    }

    public void setDifficultyType(String difficultyType) {
        this.difficultyType = difficultyType;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}