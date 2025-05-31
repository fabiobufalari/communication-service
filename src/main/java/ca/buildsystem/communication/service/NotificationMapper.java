package ca.buildsystem.communication.service;

import ca.buildsystem.communication.dto.NotificationDTO;
import ca.buildsystem.communication.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting between Notification entities and DTOs.
 * Uses MapStruct for implementation generation.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    NotificationDTO toNotificationDTO(Notification notification);

    // No mapping needed for request DTO to entity directly in mapper usually,
    // as service layer handles entity creation from request data.
    // If needed: @Mapping(target = "id", ignore = true), etc.
    // Notification toNotification(NotificationRequestDTO requestDTO);
}

