package com.group.lbstore.Model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "promotions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // Phần trăm giảm giá (0 - 100), nullable khi dùng giảm cứng
    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    // Giảm giá cứng theo số tiền (VNĐ), nullable khi dùng % giảm
    @Column(name = "fixed_discount_amount", precision = 15, scale = 2)
    private BigDecimal fixedDiscountAmount;


    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = false;

    @Column(name = "show_on_homepage", nullable = false)
    @Builder.Default
    private Boolean showOnHomepage = true;

    // Sản phẩm áp dụng (quan hệ 1-N: một promotion có nhiều product)
    // @JsonIgnoreProperties: khi serialize products, bỏ qua field 'promotion' bên trong để tránh vòng lặp
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("promotion")
    @OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    // Danh mục áp dụng (giữ lại cấu trúc nhưng không dùng ở logic chính)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "promotion_categories",
        joinColumns = @JoinColumn(name = "promotion_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @Builder.Default
    private Set<Category> categories = new HashSet<>();

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
