package ca.buildsystem.communication.service;

import ca.buildsystem.communication.dto.NotificationDTO;
import ca.buildsystem.communication.model.Notification;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-28T22:21:45-0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Ubuntu)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public NotificationDTO toNotificationDTO(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setId( notification.getId() );
        notificationDTO.setRecipient( notification.getRecipient() );
        notificationDTO.setType( notification.getType() );
        notificationDTO.setStatus( notification.getStatus() );
        notificationDTO.setSubject( notification.getSubject() );
        notificationDTO.setContent( notification.getContent() );
        notificationDTO.setErrorMessage( notification.getErrorMessage() );
        notificationDTO.setCreatedAt( notification.getCreatedAt() );
        notificationDTO.setUpdatedAt( notification.getUpdatedAt() );
        notificationDTO.setSentAt( notification.getSentAt() );

        return notificationDTO;
    }
}
