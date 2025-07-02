package com.bibek.bdfs.notification.dto;

import com.bibek.bdfs.blood_request.entity.BloodRequestEntity;
import com.bibek.bdfs.notification.entity.Notification;
import com.bibek.bdfs.notification.entity.NotificationType;
import com.bibek.bdfs.user.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class NotificationResponse {
    private Long id;
    private String donorName;
    private String message;
    private Instant sentAt;
    private NotificationBloodResponse relatedRequest;
    private NotificationType notificationType;

    public NotificationResponse(Notification notification){
        this.id = notification.getId();
        this.donorName = notification.getUser().getFullName();
        this.message = notification.getMessage();
        this.sentAt = notification.getSentAt();
        if (notification.getRelatedRequest() != null) {
            this.relatedRequest = new NotificationBloodResponse(notification.getRelatedRequest());
        }
        this.notificationType = notification.getNotificationType();
    }
}
