package com.back.handsUp.repository;

import com.back.handsUp.domain.Notification;
import com.back.handsUp.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdx(User user, Pageable pageable);
}
