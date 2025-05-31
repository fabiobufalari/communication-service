package ca.buildsystem.communication.model;

/**
 * Enum representing the status of a notification.
 */
public enum NotificationStatus {
    PENDING,    // Waiting to be sent
    SENT,       // Successfully sent
    FAILED,     // Failed to send
    DELIVERED,  // Confirmed delivery (if applicable, e.g., SMS)
    READ        // Confirmed read (if applicable, e.g., In-App)
}

