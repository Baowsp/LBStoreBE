package com.group.lbstore.DTO;

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
public class ChatMessageDTO {
    private Long id;
    private UUID roomId;
    private UUID senderId;
    private String senderName;
    private String content;
    private LocalDateTime timestamp;
}
