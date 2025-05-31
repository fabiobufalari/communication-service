package ca.buildsystem.communication.dto;

import ca.buildsystem.communication.model.NotificationStatus;
import ca.buildsystem.communication.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for representing a Notification.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private String recipient;
    private NotificationType type;
    private NotificationStatus status;
    private String subject;
    private String content;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime sentAt;
}

