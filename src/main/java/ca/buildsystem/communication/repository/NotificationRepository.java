package ca.buildsystem.communication.repository;

import ca.buildsystem.communication.model.Notification;
import ca.buildsystem.communication.model.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Notification entities.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Find notifications by status
    List<Notification> findByStatus(NotificationStatus status);

    // Find notifications by recipient
    List<Notification> findByRecipient(String recipient);

    // Find notifications by type and status
    List<Notification> findByTypeAndStatus(String type, NotificationStatus status);

    // Add other custom query methods as needed
}

