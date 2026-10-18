package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 신체 부위 설정 등록 요청")
public class UserBodyPreferenceRequest {

    @Schema(
        description = "설정을 등록할 사용자의 ID",
        example = "1"
    )
    private Long userId;

    @Schema(
        description = "사용자 설정의 유형",
        example = "UNCOMFORTABLE_BODY_PART"
    )
    private String preferenceType;

    @Schema(
        description = "설정 대상 신체 부위",
        example = "목" // 여기서, 영어가 아니라 한글로 등록해야 area와 맞아 필터가 제대로 들어감.
    )
    private String bodyArea;

    public UserBodyPreferenceRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPreferenceType() {
        return preferenceType;
    }

    public void setPreferenceType(String preferenceType) {
        this.preferenceType = preferenceType;
    }

    public String getBodyArea() {
        return bodyArea;
    }

    public void setBodyArea(String bodyArea) {
        this.bodyArea = bodyArea;
    }
}