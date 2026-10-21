package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "스트레칭 사진 정보")
public record StretchingImageResponse(

        @Schema(description = "사진 ID", example = "1")
        Long id,

        @Schema(description = "스트레칭 이름", example = "손목 위아래 움직이기")
        String stretchingName,

        @Schema(description = "동작 분류", example = "목")
        String category,

        @Schema(
                description = "사진 URL",
                example = "/images/stretching/4_updown_hands/40updown_hands.png"
        )
        String imageUrl
) {
}