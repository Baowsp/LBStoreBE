package com.group.lbstore.Service;

import com.group.lbstore.DTO.ChatMessageDTO;
import com.group.lbstore.DTO.ChatRoomDTO;
import com.group.lbstore.DTO.MessagePayload;
import com.group.lbstore.Model.ChatMessage;
import com.group.lbstore.Model.ChatRoom;
import com.group.lbstore.Model.ChatRoomStatus;
import com.group.lbstore.Model.User;
import com.group.lbstore.Repository.ChatMessageRepository;
import com.group.lbstore.Repository.ChatRoomRepository;
import com.group.lbstore.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ChatRoomDTO getOrCreateRoom(UUID customerId) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        ChatRoom room = chatRoomRepository.findByCustomerAndStatusNot(customer, ChatRoomStatus.CLOSED)
                .orElseGet(() -> {
                    ChatRoom newRoom = ChatRoom.builder()
                            .customer(customer)
                            .status(ChatRoomStatus.WAITING)
                            .isReadByCustomer(true)
                            .isReadByEmployee(false)
                            .lastMessageTime(LocalDateTime.now())
                            .build();
                    return chatRoomRepository.save(newRoom);
                });

        return mapToChatRoomDTO(room);
    }

    @Override
    public Page<ChatRoomDTO> getRoomsForAdmin(UUID adminId, Pageable pageable) {
        return chatRoomRepository.findRoomsForAdmin(ChatRoomStatus.WAITING, ChatRoomStatus.ACTIVE, pageable)
                .map(this::mapToChatRoomDTO);
    }

    @Override
    @Transactional
    public ChatRoomDTO assignAdminToRoom(UUID roomId, UUID adminId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        
        if (room.getEmployee() != null && !room.getEmployee().getId().equals(adminId)) {
            throw new RuntimeException("Phòng chat này đã có nhân viên hỗ trợ.");
        }

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (chatRoomRepository.existsByEmployeeAndStatusAndIdNot(admin, ChatRoomStatus.ACTIVE, roomId)) {
            throw new RuntimeException("Bạn đang hỗ trợ một phòng khác. Mỗi nhân viên chỉ được hỗ trợ 1 phòng cùng lúc.");
        }

        room.setEmployee(admin);
        room.setStatus(ChatRoomStatus.ACTIVE);
        chatRoomRepository.save(room);

        return mapToChatRoomDTO(room);
    }

    @Override
    public List<ChatMessageDTO> getChatHistory(UUID roomId) {
        return chatMessageRepository.findByRoomIdOrderByTimestampAsc(roomId)
                .stream()
                .map(this::mapToChatMessageDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChatMessageDTO saveMessage(MessagePayload payload) {
        ChatRoom room = chatRoomRepository.findById(payload.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        User sender = userRepository.findById(payload.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        ChatMessage message = ChatMessage.builder()
                .room(room)
                .sender(sender)
                .content(payload.getContent())
                .timestamp(LocalDateTime.now())
                .build();

        chatMessageRepository.save(message);

        // Update room status
        room.setLastMessageTime(LocalDateTime.now());
        room.setLastMessage(payload.getContent());
        if (sender.getId().equals(room.getCustomer().getId())) {
            room.setReadByCustomer(true);
            room.setReadByEmployee(false);
            if (room.getStatus() == ChatRoomStatus.CLOSED) {
                room.setStatus(ChatRoomStatus.WAITING);
                room.setEmployee(null); // Reset assignment if it was closed
            }
        } else {
            room.setReadByCustomer(false);
            room.setReadByEmployee(true);
        }
        chatRoomRepository.save(room);

        return mapToChatMessageDTO(message);
    }

    @Override
    @Transactional
    public void markRoomAsRead(UUID roomId, UUID userId, boolean isCustomer) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (isCustomer) {
            room.setReadByCustomer(true);
        } else {
            room.setReadByEmployee(true);
        }
        chatRoomRepository.save(room);
    }

    @Override
    @Transactional
    public ChatRoomDTO closeRoom(UUID roomId, UUID adminId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        if (room.getEmployee() == null || !room.getEmployee().getId().equals(adminId)) {
            throw new RuntimeException("Bạn không có quyền kết thúc phòng chat này.");
        }
        room.setStatus(ChatRoomStatus.CLOSED);
        room.setEmployee(null);
        chatRoomRepository.save(room);
        return mapToChatRoomDTO(room);
    }

    @Override
    @Transactional
    public void releaseRoom(UUID roomId, UUID adminId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        if (room.getEmployee() != null && room.getEmployee().getId().equals(adminId)) {
            room.setStatus(ChatRoomStatus.WAITING);
            room.setEmployee(null);
            chatRoomRepository.save(room);
        }
    }

    private ChatRoomDTO mapToChatRoomDTO(ChatRoom room) {
        return ChatRoomDTO.builder()
                .id(room.getId())
                .customerId(room.getCustomer().getId())
                .customerName(room.getCustomer().getFullName())
                .employeeId(room.getEmployee() != null ? room.getEmployee().getId() : null)
                .employeeName(room.getEmployee() != null ? room.getEmployee().getFullName() : null)
                .status(room.getStatus())
                .lastMessageTime(room.getLastMessageTime())
                .lastMessage(room.getLastMessage())
                .isReadByCustomer(room.isReadByCustomer())
                .isReadByEmployee(room.isReadByEmployee())
                .build();
    }

    private ChatMessageDTO mapToChatMessageDTO(ChatMessage message) {
        return ChatMessageDTO.builder()
                .id(message.getId())
                .roomId(message.getRoom().getId())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getFullName())
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .build();
    }
}
