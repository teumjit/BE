package com.example.backend.controller;

import com.example.backend.dto.StretchingImageResponse;
import com.example.backend.service.StretchingImageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stretching-images")
@Tag(
        name = "Stretching Images",
        description = "스트레칭 동작 사진 조회 API"
)
public class StretchingImageController {

    private final StretchingImageService imageService;

    public StretchingImageController(
            StretchingImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    @Operation(
            summary = "스트레칭 사진 목록 조회",
            description = "전체 사진 또는 특정 분류의 스트레칭 사진 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    public ResponseEntity<List<StretchingImageResponse>> getImages(
            @Parameter(
                    description = "사진 분류 (예: neck, shoulder, back)",
                    example = "neck"
            )
            @RequestParam(required = false) String category) {

        return ResponseEntity.ok(
                imageService.getImages(category)
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "특정 스트레칭 사진 조회",
            description = "사진 ID로 스트레칭 사진 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "사진을 찾을 수 없음")
    })
    public ResponseEntity<?> getImageById(
            @Parameter(description = "사진 ID", example = "1")
            @PathVariable Long id) {

        try {
            return ResponseEntity.ok(
                    imageService.getImageById(id)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}