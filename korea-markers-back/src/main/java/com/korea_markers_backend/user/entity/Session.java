package com.korea_markers_backend.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="sessions")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="user_id",nullable=false)
    private User user;

    @Column(nullable = false)
    private String refreshToken;

    @Column(length = 64)
    private String ipAddr;

    private boolean revoked=false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public void rotateRefreshToken(String newToken) {
        this.refreshToken = newToken;
    }
}

