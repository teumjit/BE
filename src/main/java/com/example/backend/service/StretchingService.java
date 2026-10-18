package com.example.backend.service;

import com.example.backend.domain.Stretching;
import com.example.backend.domain.StretchingDifficulty;
import com.example.backend.domain.User;
import com.example.backend.domain.UserBodyPreference;
import com.example.backend.dto.StretchingRequest;
import com.example.backend.dto.StretchingRecommendationRequest;
import com.example.backend.repository.StretchingRepository;
import com.example.backend.repository.StretchingDifficultyRepository;
import com.example.backend.repository.UserBodyPreferenceRepository;
import com.example.backend.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StretchingService {

    private final StretchingRepository stretchingRepository;
    private final StretchingDifficultyRepository difficultyRepository;
    private final UserBodyPreferenceRepository preferenceRepository;
    private final UserRepository userRepository;

    public StretchingService(
            StretchingRepository stretchingRepository,
            StretchingDifficultyRepository difficultyRepository,
            UserBodyPreferenceRepository preferenceRepository,
            UserRepository userRepository
    ) {
        this.stretchingRepository = stretchingRepository;
        this.difficultyRepository = difficultyRepository;
        this.preferenceRepository = preferenceRepository;
        this.userRepository = userRepository;
    }

    // 스트레칭 등록
    public Stretching createStretching(StretchingRequest request) {
        Stretching stretching = new Stretching(
                request.getName(),
                request.getLevel(),
                request.getArea(),
                request.getDescription(),
                request.getDurationSeconds()
        );
        return stretchingRepository.save(stretching);
    }

    // 스트레칭 전체 조회
    public List<Stretching> getAllStretchings() {
        return stretchingRepository.findAll();
    }

    // 스트레칭 ID로 조회
    public Stretching getStretching(Long id) {
        return stretchingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("스트레칭을 찾을 수 없습니다.")
                );
    }

    // 스트레칭 수정
    public Stretching updateStretching(
            Long id,
            StretchingRequest request
    ) {
        Stretching stretching = stretchingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("스트레칭을 찾을 수 없습니다.")
                );

        stretching.setName(request.getName());
        stretching.setLevel(request.getLevel());
        stretching.setArea(request.getArea());
        stretching.setDescription(request.getDescription());
        stretching.setDurationSeconds(request.getDurationSeconds());

        return stretchingRepository.save(stretching);
    }

    // 스트레칭 삭제
    public void deleteStretching(Long id) {
        Stretching stretching = stretchingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("스트레칭을 찾을 수 없습니다.")
                );

        stretchingRepository.delete(stretching);
    }

    // 맞춤형 스트레칭 추천
    @Transactional(readOnly = true)
    public List<Stretching> recommendStretchings(
            StretchingRecommendationRequest request
    ) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("userId가 필요합니다.");
        }

        if (request.getRemainingTimeSeconds() <= 0) {
            throw new IllegalArgumentException(
                    "남은 시간은 0초보다 커야 합니다."
            );
        }

        if (request.getLevel() < 0 || request.getLevel() > 2) {
            throw new IllegalArgumentException(
                    "level은 0, 1, 2 중 하나여야 합니다."
            );
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("사용자를 찾을 수 없습니다.")
                );

        // AVOID와 PREFER 부위 분리
        Set<String> avoidAreas = new HashSet<>();
        Set<String> preferAreas = new HashSet<>();

        List<UserBodyPreference> preferences =
                preferenceRepository.findByUser(user);

        for (UserBodyPreference preference : preferences) {
            String area = normalizeArea(preference.getBodyArea());
            String type = preference.getPreferenceType();

            if ("AVOID".equalsIgnoreCase(type)) {
                avoidAreas.add(area);
            } else if ("PREFER".equalsIgnoreCase(type)) {
                preferAreas.add(area);
            }
        }

        // AVOID가 PREFER보다 우선
        preferAreas.removeAll(avoidAreas);

        // 스트레칭별 제약 태그를 한 번에 조회
        Map<Long, List<StretchingDifficulty>> difficultyMap =
                difficultyRepository.findAll().stream()
                        .collect(Collectors.groupingBy(
                                StretchingDifficulty::getStretchingId
                        ));

        List<String> constraints = request.getConstraints() == null
                ? List.of()
                : request.getConstraints();

        List<Stretching> candidates = stretchingRepository.findAll()
                .stream()
                .filter(s -> s.getLevel() == request.getLevel())
                .filter(s -> s.getDurationSeconds() > 0)
                .filter(s -> !avoidAreas.contains(
                        normalizeArea(s.getArea())
                ))
                .filter(s -> matchesCondition(
                        difficultyMap.getOrDefault(s.getId(), List.of()),
                        "POSTURE",
                        request.getPosture()
                ))
                .filter(s -> matchesCondition(
                        difficultyMap.getOrDefault(s.getId(), List.of()),
                        "LOCATION",
                        request.getLocation()
                ))
                .filter(s -> matchesConstraints(
                        difficultyMap.getOrDefault(s.getId(), List.of()),
                        constraints
                ))
                .sorted(
                        Comparator
                                .comparing((Stretching s) ->
                                        !preferAreas.contains(
                                                normalizeArea(s.getArea())
                                        )
                                )
                                .thenComparingInt(
                                        Stretching::getDurationSeconds
                                )
                )
                .toList();

        // 남은 시간 안에 들어가는 스트레칭 선택
        List<Stretching> result = new ArrayList<>();
        int remaining = request.getRemainingTimeSeconds();

        for (Stretching stretching : candidates) {
            int duration = stretching.getDurationSeconds();

            if (duration <= remaining) {
                result.add(stretching);
                remaining -= duration;
            }
        }

        return result;
    }

    // 해당 유형의 태그가 등록되어 있다면 요청값과 일치해야 함.
    // 해당 유형의 태그 자체가 없는 기존 데이터는 통과시킴.
    private boolean matchesCondition(
            List<StretchingDifficulty> difficulties,
            String type,
            String requestedValue
    ) {
        if (requestedValue == null || requestedValue.isBlank()) {
            return true;
        }

        List<String> values = difficulties.stream()
                .filter(d -> type.equalsIgnoreCase(
                        d.getDifficultyType()
                ))
                .map(StretchingDifficulty::getDifficulty)
                .toList();

        return values.isEmpty()
                || values.stream().anyMatch(
                        value -> requestedValue.equalsIgnoreCase(value)
                );
    }

    // 사용자가 선택한 제약 조건을 모두 포함하는지 확인
    private boolean matchesConstraints(
            List<StretchingDifficulty> difficulties,
            List<String> requestedConstraints
    ) {
        if (requestedConstraints.isEmpty()) {
            return true;
        }

        Set<String> available = difficulties.stream()
                .filter(d -> "CONSTRAINT".equalsIgnoreCase(
                        d.getDifficultyType()
                ))
                .map(StretchingDifficulty::getDifficulty)
                .filter(value -> value != null)
                .map(value -> value.toUpperCase(Locale.ROOT))
                .collect(Collectors.toSet());

        return available.containsAll(
                requestedConstraints.stream()
                        .filter(value -> value != null)
                        .map(value -> value.toUpperCase(Locale.ROOT))
                        .toList()
        );
    }

    // DB의 신체 부위 값과 스트레칭 area 값을 비교하기 위한 정규화
    private String normalizeArea(String area) {
        if (area == null) {
            return "";
        }

        String value = area.trim().toUpperCase(Locale.ROOT);

        return switch (value) {
            case "목", "목 부위" -> "목";
            case "어깨", "어깨 부위" -> "어깨";
            case "허리", "허리 부위", "아래 허리", "등" -> "허리";
            case "다리", "다리 부위" -> "다리";
            case "손", "손 부위" -> "손";
            default -> value;
        };
    }
}
