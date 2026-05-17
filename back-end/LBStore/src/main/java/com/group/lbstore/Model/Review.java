package com.group.lbstore.Model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rating", nullable = false)
    private Integer rating; // Số sao đánh giá (thường từ 1 đến 5)

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment; // Nội dung đánh giá của khách hàng

    @Column(name = "is_verified_purchase", nullable = false)
    @Builder.Default
    private boolean isVerifiedPurchase = false; // Đánh dấu true nếu khách hàng đã thực sự mua sản phẩm này

    // Quan hệ Many-To-One với Product (Nhiều đánh giá thuộc về một sản phẩm)
    // Bỏ comment khi bạn đã tạo class Product

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


    // Quan hệ Many-To-One với User (Một khách hàng có thể viết nhiều đánh giá)
    // Bỏ comment khi bạn đã tạo class User

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        // Đảm bảo rating luôn nằm trong khoảng 1-5
        if (this.rating == null || this.rating < 1) this.rating = 1;
        if (this.rating > 5) this.rating = 5;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}