package com.group.lbstore.Service;

import com.group.lbstore.DTO.ChatMessageDTO;
import com.group.lbstore.DTO.ChatRoomDTO;
import com.group.lbstore.DTO.MessagePayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    ChatRoomDTO getOrCreateRoom(UUID customerId);
    Page<ChatRoomDTO> getRoomsForAdmin(UUID adminId, Pageable pageable);
    ChatRoomDTO assignAdminToRoom(UUID roomId, UUID adminId);
    List<ChatMessageDTO> getChatHistory(UUID roomId);
    ChatMessageDTO saveMessage(MessagePayload payload);
    void markRoomAsRead(UUID roomId, UUID userId, boolean isCustomer);
    ChatRoomDTO closeRoom(UUID roomId, UUID adminId);
    void releaseRoom(UUID roomId, UUID adminId);
}
