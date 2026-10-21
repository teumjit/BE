package com.example.backend.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stretching_history")
public class StretchingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이 기록을 수행한 사용자
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 스트레칭 이름을 기록 당시 기준으로 보존
    @Column(nullable = false, length = 100)
    private String stretchingName;

    // 실제 수행 시간(초)
    @Column(nullable = false)
    private Integer durationSeconds;

    // 스트레칭 완료 시각
    @Column(nullable = false)
    private LocalDateTime completedAt;

    protected StretchingHistory() {
    }

    public StretchingHistory(
            User user,
            String stretchingName,
            Integer durationSeconds,
            LocalDateTime completedAt) {
        this.user = user;
        this.stretchingName = stretchingName;
        this.durationSeconds = durationSeconds;
        this.completedAt = completedAt;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getStretchingName() {
        return stretchingName;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
}