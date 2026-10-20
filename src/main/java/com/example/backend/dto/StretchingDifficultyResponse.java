package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "스트레칭 난이도 및 제약 조건 조회 응답")
public class StretchingDifficultyResponse {

    @Schema(
        description = "난이도 또는 제약 조건 데이터의 ID",
        example = "1"
    )
    private Long id;

    @Schema(
        description = "해당 조건이 연결된 스트레칭의 ID",
        example = "1"
    )
    private Long stretchingId;

    @Schema(
        description = "난이도 또는 제약 조건의 유형",
        example = "REQUIREMENT"
    )
    private String difficultyType;

    @Schema(
        description = "난이도 또는 제약 조건의 값",
        example = "ONE_HANDS"
    )
    private String difficulty;

    public StretchingDifficultyResponse(
            Long id,
            Long stretchingId,
            String difficultyType,
            String difficulty
    ) {
        this.id = id;
        this.stretchingId = stretchingId;
        this.difficultyType = difficultyType;
        this.difficulty = difficulty;
    }

    public Long getId() {
        return id;
    }

    public Long getStretchingId() {
        return stretchingId;
    }

    public String getDifficultyType() {
        return difficultyType;
    }

    public String getDifficulty() {
        return difficulty;
    }
}