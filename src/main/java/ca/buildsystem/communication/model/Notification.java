package ca.buildsystem.communication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a notification record.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String recipient; // Email address or phone number

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status = NotificationStatus.PENDING;

    @Column
    private String subject; // Primarily for emails

    @Lob // Use Lob for potentially large content
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column
    private String errorMessage; // Store error details if sending failed

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp // Tracks the last update, useful for status changes
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime sentAt; // Timestamp when the notification was successfully sent

    // Optional: Add fields for sender, related entity ID (e.g., report ID, user ID), etc.
    // private String sender;
    // private String relatedEntityId;
}

