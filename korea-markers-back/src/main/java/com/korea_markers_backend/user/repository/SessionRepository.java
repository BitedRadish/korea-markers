package com.korea_markers_backend.user.repository;

import com.korea_markers_backend.user.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session,Long> {
    Optional<Session> findByRefreshToken(String refreshToken);
    void deleteByUser_IdAndRefreshToken(Long Id,String refreshToken);
    Optional<List<Session>> findByUser_IdAndRefreshToken(Long userId, String refreshToken);
}
