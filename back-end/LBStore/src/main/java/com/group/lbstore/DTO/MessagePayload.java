package com.group.lbstore.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagePayload {
    private UUID roomId;
    private UUID senderId;
    private String content;
}
