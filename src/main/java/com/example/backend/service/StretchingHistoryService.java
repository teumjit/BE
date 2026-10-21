package com.example.backend.service;

import com.example.backend.domain.StretchingHistory;
import com.example.backend.domain.User;
import com.example.backend.dto.StretchingHistoryRequest;
import com.example.backend.dto.TodayStretchingSummaryResponse;
import com.example.backend.repository.StretchingHistoryRepository;
import com.example.backend.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class StretchingHistoryService {

    private static final ZoneId KOREA_ZONE =
            ZoneId.of("Asia/Seoul");

    private final StretchingHistoryRepository historyRepository;
    private final UserRepository userRepository;

    public StretchingHistoryService(
            StretchingHistoryRepository historyRepository,
            UserRepository userRepository) {
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public StretchingHistory saveHistory(
            String authenticatedEmail,
            StretchingHistoryRequest request) {

        if (authenticatedEmail == null
                || authenticatedEmail.isBlank()) {
            throw new IllegalArgumentException(
                    "로그인한 사용자 정보를 확인할 수 없습니다.");
        }

        if (request == null
                || request.getStretchingName() == null
                || request.getStretchingName().isBlank()
                || request.getDurationSeconds() == null
                || request.getDurationSeconds() < 1
                || request.getDurationSeconds() > 3600) {
            throw new IllegalArgumentException(
                    "스트레칭 이름과 수행 시간을 올바르게 입력해 주세요.");
        }

        String email = authenticatedEmail.trim()
                .toLowerCase(java.util.Locale.ROOT);

        User user = userRepository.findByUserId(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "사용자를 찾을 수 없습니다."));

        StretchingHistory history = new StretchingHistory(
                user,
                request.getStretchingName().trim(),
                request.getDurationSeconds(),
                LocalDateTime.now(KOREA_ZONE)
        );

        return historyRepository.save(history);
    }

    @Transactional(readOnly = true)
    public TodayStretchingSummaryResponse getTodaySummary(
            String authenticatedEmail) {

        if (authenticatedEmail == null
                || authenticatedEmail.isBlank()) {
            throw new IllegalArgumentException(
                    "로그인한 사용자 정보를 확인할 수 없습니다.");
        }

        String email = authenticatedEmail.trim()
                .toLowerCase(java.util.Locale.ROOT);

        User user = userRepository.findByUserId(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "사용자를 찾을 수 없습니다."));

        LocalDate today = LocalDate.now(KOREA_ZONE);
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        Long totalSeconds =
                historyRepository.sumDurationByUserAndPeriod(
                        user.getId(), start, end);

        long count = historyRepository.countByUserAndPeriod(
                user.getId(), start, end);

        return new TodayStretchingSummaryResponse(
                today,
                totalSeconds == null ? 0L : totalSeconds,
                count
        );
    }

    
        /**
         * 사용자 ID로 스트레칭 기록 저장
         */
        @Transactional
        public StretchingHistory saveHistory(
                Long userId,
                StretchingHistoryRequest request) {

        if (userId == null) {
                throw new IllegalArgumentException("사용자 ID가 필요합니다.");
        }

        if (request == null
                || request.getStretchingName() == null
                || request.getStretchingName().isBlank()
                || request.getDurationSeconds() == null
                || request.getDurationSeconds() < 1
                || request.getDurationSeconds() > 3600) {
                throw new IllegalArgumentException(
                        "스트레칭 이름과 수행 시간을 올바르게 입력해 주세요.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        StretchingHistory history = new StretchingHistory(
                user,
                request.getStretchingName().trim(),
                request.getDurationSeconds(),
                LocalDateTime.now(KOREA_ZONE)
        );

        return historyRepository.save(history);
        }

        /**
         * 사용자 ID로 스트레칭 기록 목록 조회
         */
        @Transactional(readOnly = true)
        public java.util.List<StretchingHistory> getHistories(Long userId) {

        if (userId == null || !userRepository.existsById(userId)) {
                throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        return historyRepository
                .findAllByUser_IdOrderByCompletedAtDesc(userId);
        }

        /**
         * 사용자 ID로 오늘의 스트레칭 요약 조회
         */
        @Transactional(readOnly = true)
        public TodayStretchingSummaryResponse getTodaySummary(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        LocalDate today = LocalDate.now(KOREA_ZONE);
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        Long totalSeconds = historyRepository.sumDurationByUserAndPeriod(
                user.getId(), start, end);

        long count = historyRepository.countByUserAndPeriod(
                user.getId(), start, end);

        return new TodayStretchingSummaryResponse(
                today,
                totalSeconds == null ? 0L : totalSeconds,
                count
        );
        }

}