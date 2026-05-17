package com.group.lbstore.Model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vouchers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    // Giảm theo % giá trị
    @Column(name = "discount_percentage", precision = 5, scale = 2, nullable = false)
    private BigDecimal discountPercentage;

    // Giảm tối đa bao nhiêu tiền
    @Column(name = "max_discount_amount", precision = 15, scale = 2)
    private BigDecimal maxDiscountAmount;

    // Đơn hàng tối thiểu bao nhiêu tiền thì áp dụng được
    @Column(name = "min_order_value", precision = 15, scale = 2)
    private BigDecimal minOrderValue;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    // Tổng số lượt sử dụng tối đa
    @Column(name = "usage_limit")
    @Builder.Default
    private Integer usageLimit = 0;

    // Đã sử dụng bao nhiêu lượt
    @Column(name = "used_count")
    @Builder.Default
    private Integer usedCount = 0;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.code != null) {
            this.code = this.code.toUpperCase().trim();
        }
        if (this.usedCount == null) this.usedCount = 0;
        if (this.usageLimit == null) this.usageLimit = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (this.code != null) {
            this.code = this.code.toUpperCase().trim();
        }
    }
}
