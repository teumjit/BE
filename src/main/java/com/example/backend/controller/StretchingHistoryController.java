
package com.example.backend.controller;

import com.example.backend.domain.StretchingHistory;
import com.example.backend.dto.StretchingHistoryRequest;
import com.example.backend.dto.TodayStretchingSummaryResponse;
import com.example.backend.service.StretchingHistoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stretching-history")
@Tag(name = "Stretching History",
     description = "스트레칭 수행 기록 및 통계 API")
public class StretchingHistoryController {

    private final StretchingHistoryService historyService;

    public StretchingHistoryController(
            StretchingHistoryService historyService) {
        this.historyService = historyService;
    }

    @PostMapping("/{userId}")
    @Operation(summary = "스트레칭 수행 기록 저장")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "저장 성공"),
        @ApiResponse(responseCode = "400", description = "요청 데이터 오류")
    })
    public ResponseEntity<?> saveHistory(
            @PathVariable Long userId,
            @Valid @RequestBody StretchingHistoryRequest request) {

        StretchingHistory history =
                historyService.saveHistory(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                    "message", "스트레칭 기록이 저장되었습니다.",
                    "id", history.getId(),
                    "userId", userId,
                    "stretchingName", history.getStretchingName(),
                    "durationSeconds", history.getDurationSeconds(),
                    "completedAt", history.getCompletedAt()
                )
        );
    }

    @GetMapping("/{userId}")
    @Operation(summary = "사용자의 스트레칭 기록 목록 조회")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    public ResponseEntity<?> getHistories(
            @PathVariable Long userId) {

        List<Map<String, Object>> result =
                historyService.getHistories(userId)
                        .stream()
                        .map(history -> Map.<String, Object>of(
                                "id", history.getId(),
                                "stretchingName", history.getStretchingName(),
                                "durationSeconds", history.getDurationSeconds(),
                                "completedAt", history.getCompletedAt()
                        ))
                        .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{userId}/today")
    @Operation(summary = "오늘의 총 스트레칭 수행 시간 조회")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    public ResponseEntity<TodayStretchingSummaryResponse> getTodaySummary(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                historyService.getTodaySummary(userId));
    }
}
