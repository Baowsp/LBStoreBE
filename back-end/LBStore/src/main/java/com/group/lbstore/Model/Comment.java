package com.group.lbstore.Model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content; // Nội dung câu hỏi hoặc câu trả lời

    @Column(name = "stars", nullable = false)
    @Builder.Default
    private int stars = 5; // Điểm đánh giá (1-5 sao)

    // Trạng thái kiểm duyệt: Mặc định là true (để dễ test)
    @Column(name = "is_approved", nullable = false)
    @Builder.Default
    private boolean isApproved = true;

    // Quan hệ Many-To-One với Product (Bình luận này thuộc về sản phẩm nào)
    // Bỏ comment khi đã có class Product

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


    // Quan hệ Many-To-One với User (Ai là người bình luận: Khách hàng hoặc Admin)
    // Bỏ comment khi đã có class User

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    // --- CẤU TRÚC CHA CON (SELF-REFERENCING) ---

    // Chỉ ra Comment này là câu trả lời cho Comment nào (Parent)
    // Nếu parent == null, đây là bình luận gốc (Câu hỏi mới)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    // Danh sách các câu trả lời (Replies) của bình luận này
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> replies = new ArrayList<>();

    // ---------------------------------------------

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}