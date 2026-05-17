package com.group.lbstore.DTO;

import lombok.Data;
import java.util.UUID;

@Data
public class CommentRequest {
    private String content;
    private int stars;
    private Long productId;
    private UUID userId; // AuthController usually returns user with UUID
    private Long parentId; // ID of the comment being replied to
}
