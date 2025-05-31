package ca.buildsystem.communication.service;

import ca.buildsystem.communication.dto.NotificationDTO;
import ca.buildsystem.communication.dto.NotificationRequestDTO;
import ca.buildsystem.communication.exception.ResourceNotFoundException;
import ca.buildsystem.communication.model.Notification;
import ca.buildsystem.communication.model.NotificationStatus;
import ca.buildsystem.communication.model.NotificationType;
import ca.buildsystem.communication.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Implementation of the NotificationService.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final JavaMailSender mailSender;
    // Inject SMS client if needed (e.g., TwilioRestClient)

    @Value("${spring.mail.username}") // Get sender email from properties
    private String mailSenderAddress;

    // TODO: Add Twilio properties if using Twilio for SMS
    // @Value("${twilio.accountSid}")
    // private String twilioAccountSid;
    // @Value("${twilio.authToken}")
    // private String twilioAuthToken;
    // @Value("${twilio.phoneNumber}")
    // private String twilioPhoneNumber;

    @Override
    @Transactional
    public NotificationDTO sendNotification(NotificationRequestDTO requestDTO) {
        log.info("Received notification request for recipient: {}, type: {}", requestDTO.getRecipient(), requestDTO.getType());

        // 1. Create and save the initial notification record
        Notification notification = new Notification();
        notification.setRecipient(requestDTO.getRecipient());
        notification.setType(requestDTO.getType());
        notification.setSubject(requestDTO.getSubject());
        notification.setContent(requestDTO.getContent());
        notification.setStatus(NotificationStatus.PENDING);
        // notification.setSender(requestDTO.getSender()); // If sender is included
        // notification.setRelatedEntityId(requestDTO.getRelatedEntityId()); // If related ID is included

        Notification savedNotification = notificationRepository.save(notification);
        log.info("Saved initial notification record with ID: {}", savedNotification.getId());

        // 2. Attempt to send the notification (can be made asynchronous)
        try {
            switch (requestDTO.getType()) {
                case EMAIL:
                    sendEmailNotification(savedNotification);
                    break;
                case SMS:
                    sendSmsNotification(savedNotification);
                    break;
                case IN_APP:
                    // Handle In-App notification logic (e.g., save to a different table, push via WebSocket)
                    log.warn("In-App notification type not fully implemented yet for ID: {}", savedNotification.getId());
                    // For now, just mark as sent for demo purposes
                    savedNotification.setStatus(NotificationStatus.SENT);
                    savedNotification.setSentAt(LocalDateTime.now());
                    break;
                default:
                    log.error("Unsupported notification type: {} for ID: {}", requestDTO.getType(), savedNotification.getId());
                    savedNotification.setStatus(NotificationStatus.FAILED);
                    savedNotification.setErrorMessage("Unsupported notification type");
            }
        } catch (Exception e) {
            log.error("Failed to send notification ID: {}. Error: {}", savedNotification.getId(), e.getMessage(), e);
            savedNotification.setStatus(NotificationStatus.FAILED);
            savedNotification.setErrorMessage(e.getMessage());
        }

        // 3. Update the notification record with the final status
        Notification updatedNotification = notificationRepository.save(savedNotification);
        log.info("Updated notification ID: {} with status: {}", updatedNotification.getId(), updatedNotification.getStatus());

        return notificationMapper.toNotificationDTO(updatedNotification);
    }

    private void sendEmailNotification(Notification notification) {
        log.info("Attempting to send EMAIL notification ID: {} to {}", notification.getId(), notification.getRecipient());
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailSenderAddress);
            message.setTo(notification.getRecipient());
            message.setSubject(notification.getSubject() != null ? notification.getSubject() : "Notification from Build System");
            message.setText(notification.getContent());
            mailSender.send(message);
            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
            log.info("Successfully sent EMAIL notification ID: {}", notification.getId());
        } catch (MailException e) {
            log.error("Failed to send EMAIL notification ID: {}. Error: {}", notification.getId(), e.getMessage(), e);
            notification.setStatus(NotificationStatus.FAILED);
            notification.setErrorMessage("Email sending failed: " + e.getMessage());
            // Re-throw or handle specific mail exceptions if needed
            throw e;
        }
    }

    private void sendSmsNotification(Notification notification) {
        log.info("Attempting to send SMS notification ID: {} to {}", notification.getId(), notification.getRecipient());
        // TODO: Implement SMS sending logic using Twilio or another provider
        log.warn("SMS sending not implemented yet for notification ID: {}", notification.getId());
        // Placeholder: Mark as failed until implemented
        notification.setStatus(NotificationStatus.FAILED);
        notification.setErrorMessage("SMS functionality not implemented");
        // Example with Twilio (requires Twilio library and credentials):
        /*
        try {
            Twilio.init(twilioAccountSid, twilioAuthToken);
            Message message = Message.creator(
                    new com.twilio.type.PhoneNumber(notification.getRecipient()), // To
                    new com.twilio.type.PhoneNumber(twilioPhoneNumber), // From
                    notification.getContent())
                .create();
            log.info("SMS sent with SID: {} for notification ID: {}", message.getSid(), notification.getId());
            notification.setStatus(NotificationStatus.SENT); // Or DELIVERED if status callback is used
            notification.setSentAt(LocalDateTime.now());
        } catch (ApiException e) {
            log.error("Failed to send SMS notification ID: {}. Twilio Error Code: {}, Message: {}", notification.getId(), e.getCode(), e.getMessage(), e);
            notification.setStatus(NotificationStatus.FAILED);
            notification.setErrorMessage("SMS sending failed: " + e.getMessage());
            throw e;
        }
        */
    }

    @Override
    public NotificationDTO getNotificationById(Long id) {
        log.debug("Fetching notification by ID: {}", id);
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        return notificationMapper.toNotificationDTO(notification);
    }

    @Override
    public Page<NotificationDTO> getAllNotifications(Pageable pageable) {
        log.debug("Fetching all notifications with pagination: {}", pageable);
        Page<Notification> notificationPage = notificationRepository.findAll(pageable);
        return notificationPage.map(notificationMapper::toNotificationDTO);
    }
}

