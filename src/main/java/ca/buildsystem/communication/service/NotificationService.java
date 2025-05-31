package ca.buildsystem.communication.service;

import ca.buildsystem.communication.dto.NotificationDTO;
import ca.buildsystem.communication.dto.NotificationRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing and sending notifications.
 */
public interface NotificationService {

    /**
     * Sends a notification based on the request details.
     *
     * @param requestDTO The details of the notification to send.
     * @return The created NotificationDTO with its initial status.
     */
    NotificationDTO sendNotification(NotificationRequestDTO requestDTO);

    /**
     * Retrieves a notification by its ID.
     *
     * @param id The ID of the notification.
     * @return The NotificationDTO.
     * @throws ResourceNotFoundException if the notification is not found.
     */
    NotificationDTO getNotificationById(Long id);

    /**
     * Retrieves all notifications with pagination.
     *
     * @param pageable Pagination information.
     * @return A page of NotificationDTOs.
     */
    Page<NotificationDTO> getAllNotifications(Pageable pageable);

    // Add methods for retrying failed notifications, getting notifications by status/recipient etc. if needed
}

