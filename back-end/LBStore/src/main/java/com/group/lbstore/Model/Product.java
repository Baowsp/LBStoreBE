package com.group.lbstore.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "specs", columnDefinition = "TEXT")
    private String specs;

    @Column(name = "image_url", columnDefinition = "TEXT")
    @com.fasterxml.jackson.annotation.JsonProperty("imageURL")
    private String imageURL;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id", nullable = false)
    @org.hibernate.annotations.NotFound(action = org.hibernate.annotations.NotFoundAction.IGNORE)
    private Brand brand;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @org.hibernate.annotations.NotFound(action = org.hibernate.annotations.NotFoundAction.IGNORE)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 30)
    private List<ProductVariant> variants;

    @Column(name = "slug", nullable = false)
    private String slug;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "stock_quantity", nullable = true)
    private Integer stockQuantity;

    // Khuyến mãi áp dụng cho sản phẩm này (nullable — null = không có khuyến mãi)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("products")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "promotion_id", nullable = true)
    private Promotion promotion;

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