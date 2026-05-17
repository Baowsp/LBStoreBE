package com.group.lbstore.Service;

import com.group.lbstore.DTO.CommentRequest;
import com.group.lbstore.DTO.CommentResponse;
import com.group.lbstore.Model.Comment;
import com.group.lbstore.Model.Product;
import com.group.lbstore.Model.User;
import com.group.lbstore.Repository.CommentRepository;
import com.group.lbstore.Repository.ProductRepository;
import com.group.lbstore.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;
    private final com.group.lbstore.Repository.OnlineOrderRepository onlineOrderRepository;

    @Override
    public CommentResponse createComment(CommentRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + request.getProductId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .stars(request.getStars())
                .product(product)
                .user(user)
                .isApproved(true) // Tự động hiển thị theo yêu cầu
                .build();

        if (request.getParentId() != null) {
            Comment parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
            comment.setParent(parent);
        }

        Comment saved = commentRepository.save(comment);
        return mapToResponse(saved);
    }

    @Override
    public List<CommentResponse> findByProductId(Long productId) {
        // Chỉ lấy các bình luận gốc để bắt đầu xây dựng cây đệ quy
        List<Comment> rootComments = commentRepository.findByProductIdAndParentIsNull(productId);
        return rootComments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public boolean canComment(java.util.UUID userId, Long productId) {
        try {
            return onlineOrderRepository.hasUserPurchasedProduct(userId, productId);
        } catch (Exception e) {
            return false;
        }
    }

    private CommentResponse mapToResponse(Comment comment) {
        String name = comment.getUser().getFullName();
        if (name == null || name.trim().isEmpty()) {
            name = comment.getUser().getEmail().split("@")[0]; // Fallback to email prefix
        }

        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .stars(comment.getStars())
                .isApproved(comment.isApproved())
                .userName(name)
                .userId(comment.getUser().getId())
                .replies(comment.getReplies() != null
                        ? comment.getReplies().stream().map(this::mapToResponse).collect(Collectors.toList())
                        : java.util.Collections.emptyList())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}