package com.group.lbstore.DTO;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponse {
    private Long id;
    private String content;
    private int stars;
    private boolean isApproved;
    private String userName; // Just the name, not the whole User object + password
    private java.util.UUID userId; // To check if current user can delete? No, but useful
    private java.util.List<CommentResponse> replies;
    private LocalDateTime createdAt;
}
