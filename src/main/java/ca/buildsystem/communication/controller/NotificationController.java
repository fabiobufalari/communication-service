package ca.buildsystem.communication.controller;

import ca.buildsystem.communication.dto.NotificationDTO;
import ca.buildsystem.communication.dto.NotificationRequestDTO;
import ca.buildsystem.communication.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing notifications.
 */
@RestController
@RequestMapping("/api/v1/notifications") // Using versioning in the path
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "API for sending and managing notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Send a new notification", description = "Creates a notification record and attempts to send it via the specified channel (EMAIL, SMS).")
    @ApiResponse(responseCode = "201", description = "Notification request accepted and processed")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    public ResponseEntity<NotificationDTO> sendNotification(@Valid @RequestBody NotificationRequestDTO requestDTO) {
        NotificationDTO createdNotification = notificationService.sendNotification(requestDTO);
        // Return 201 Created status along with the initial status of the notification
        return new ResponseEntity<>(createdNotification, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get notification by ID", description = "Retrieves the details and status of a specific notification.")
    @ApiResponse(responseCode = "200", description = "Notification found")
    @ApiResponse(responseCode = "404", description = "Notification not found")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable Long id) {
        NotificationDTO notificationDTO = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notificationDTO);
    }

    @GetMapping
    @Operation(summary = "Get all notifications", description = "Retrieves a paginated list of all notification records.")
    @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully")
    public ResponseEntity<Page<NotificationDTO>> getAllNotifications(
            @PageableDefault(size = 20, sort = "createdAt,desc") Pageable pageable) {
        Page<NotificationDTO> notificationPage = notificationService.getAllNotifications(pageable);
        return ResponseEntity.ok(notificationPage);
    }

    // TODO: Add endpoints for retrying failed notifications, searching/filtering, etc. if needed

}

