package com.group.lbstore.DTO;

import com.group.lbstore.Model.ChatRoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDTO {
    private UUID id;
    private UUID customerId;
    private String customerName;
    private UUID employeeId;
    private String employeeName;
    private ChatRoomStatus status;
    private LocalDateTime lastMessageTime;
    private String lastMessage;
    private boolean isReadByCustomer;
    private boolean isReadByEmployee;
}
