package com.example.backend.repository;

import com.example.backend.domain.StretchingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StretchingHistoryRepository
        extends JpaRepository<StretchingHistory, Long> {

    @Query("""
        SELECT COALESCE(SUM(h.durationSeconds), 0)
        FROM StretchingHistory h
        WHERE h.user.id = :userId
          AND h.completedAt >= :start
          AND h.completedAt < :end
        """)
    Long sumDurationByUserAndPeriod(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
        SELECT COUNT(h)
        FROM StretchingHistory h
        WHERE h.user.id = :userId
          AND h.completedAt >= :start
          AND h.completedAt < :end
        """)
    long countByUserAndPeriod(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
    List<StretchingHistory> findAllByUser_IdOrderByCompletedAtDesc(Long userId);
}