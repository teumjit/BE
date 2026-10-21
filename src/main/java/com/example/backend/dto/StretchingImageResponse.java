package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "스트레칭 사진 정보")
public record StretchingImageResponse(

        @Schema(description = "사진 ID", example = "1")
        Long id,

        @Schema(description = "스트레칭 이름", example = "목 스트레칭")
        String stretchingName,

        @Schema(description = "동작 분류", example = "neck")
        String category,

        @Schema(
                description = "사진 URL",
                example = "/images/stretching/neck/neck-stretch-01.jpg"
        )
        String imageUrl
) {
}