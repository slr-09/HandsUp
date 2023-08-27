package com.back.handsUp.dto;

import com.back.handsUp.domain.Notification;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NotificationResponse {
    private String title;
    private String body;
    private LocalDateTime createdAt;

    public NotificationResponse(String title, String body, LocalDateTime createdAt) {
        this.title = title;
        this.body = body;
        this.createdAt = createdAt;
    }

    public static NotificationResponse entityToDto(Notification notification) {
        return new NotificationResponse(
                notification.getTitle(),
                notification.getBody(),
                notification.getCreatedAt()
        );
    }
}
