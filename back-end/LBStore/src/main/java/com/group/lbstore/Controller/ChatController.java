package com.group.lbstore.Controller;

import com.group.lbstore.DTO.ChatMessageDTO;
import com.group.lbstore.DTO.ChatRoomDTO;
import com.group.lbstore.DTO.MessagePayload;
import com.group.lbstore.Service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    // WebSocket Endpoint: Client sends messages to /app/chat.sendMessage
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload MessagePayload chatMessage) {
        // Save to DB
        ChatMessageDTO savedMessage = chatService.saveMessage(chatMessage);
        
        // Broadcast to room topic: /topic/room/{roomId}
        messagingTemplate.convertAndSend("/topic/room/" + chatMessage.getRoomId(), savedMessage);
    }

    // REST API for Customer
    @GetMapping("/api/chat/room")
    public ResponseEntity<ChatRoomDTO> getOrCreateCustomerRoom(@RequestParam UUID customerId) {
        return ResponseEntity.ok(chatService.getOrCreateRoom(customerId));
    }

    // REST API for Admin/Staff
    @GetMapping("/api/admin/chat/rooms")
    public ResponseEntity<Page<ChatRoomDTO>> getAdminRooms(
            @RequestParam UUID adminId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(chatService.getRoomsForAdmin(adminId, PageRequest.of(page, size)));
    }

    @PostMapping("/api/admin/chat/rooms/{roomId}/assign")
    public ResponseEntity<ChatRoomDTO> assignAdminToRoom(
            @PathVariable UUID roomId,
            @RequestParam UUID adminId) {
        try {
            return ResponseEntity.ok(chatService.assignAdminToRoom(roomId, adminId));
        } catch (RuntimeException e) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PostMapping("/api/admin/chat/rooms/{roomId}/close")
    public ResponseEntity<ChatRoomDTO> closeRoom(
            @PathVariable UUID roomId,
            @RequestParam UUID adminId) {
        try {
            return ResponseEntity.ok(chatService.closeRoom(roomId, adminId));
        } catch (RuntimeException e) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PostMapping("/api/admin/chat/rooms/{roomId}/release")
    public ResponseEntity<Void> releaseRoom(
            @PathVariable UUID roomId,
            @RequestParam UUID adminId) {
        chatService.releaseRoom(roomId, adminId);
        return ResponseEntity.ok().build();
    }

    // Common REST APIs
    @GetMapping("/api/chat/rooms/{roomId}/messages")
    public ResponseEntity<List<ChatMessageDTO>> getChatHistory(@PathVariable UUID roomId) {
        return ResponseEntity.ok(chatService.getChatHistory(roomId));
    }

    @PostMapping("/api/chat/rooms/{roomId}/read")
    public ResponseEntity<Void> markRoomAsRead(
            @PathVariable UUID roomId,
            @RequestParam UUID userId,
            @RequestParam boolean isCustomer) {
        chatService.markRoomAsRead(roomId, userId, isCustomer);
        return ResponseEntity.ok().build();
    }
}
