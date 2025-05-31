package ca.buildsystem.communication.dto;

import ca.buildsystem.communication.model.NotificationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new Notification request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDTO {

    @NotBlank(message = "Recipient cannot be blank")
    // Add @Email validation if type is EMAIL, or pattern for phone if SMS
    private String recipient;

    @NotNull(message = "Notification type cannot be null")
    private NotificationType type;

    // Subject is required for EMAIL type
    private String subject; 

    @NotBlank(message = "Content cannot be blank")
    private String content;

    // Optional fields for future use
    // private String sender;
    // private String relatedEntityId;
}

