package com.group.lbstore.Service;

import com.group.lbstore.DTO.CommentRequest;
import com.group.lbstore.DTO.CommentResponse;
import java.util.List;

public interface CommentService {
    CommentResponse createComment(CommentRequest request);
    List<CommentResponse> findByProductId(Long productId);
    void deleteComment(Long id);
    boolean canComment(java.util.UUID userId, Long productId);
}