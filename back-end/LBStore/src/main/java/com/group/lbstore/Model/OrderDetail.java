package com.group.lbstore.Model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ Many-To-One: Dòng chi tiết này thuộc về Đơn hàng nào?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Quan hệ Many-To-One: Khách mua màu sắc + dung lượng cụ thể nào?
    // VD: iPhone 15 Pro Max 256GB - Titan Đen
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_color_id", nullable = false)
    private ProductVariantColor variantColor;

    // Số lượng khách mua (VD: Mua 2 chiếc)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    // QUAN TRỌNG: Giá bán của 1 sản phẩm TẠI THỜI ĐIỂM MUA
    @Column(name = "unit_price", precision = 15, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    // Số tiền giảm giá cho riêng dòng sản phẩm này (nếu có)
    @Column(name = "discount_amount", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    // Thành tiền của dòng này = (quantity * unitPrice) - discountAmount
    @Column(name = "sub_total", precision = 15, scale = 2, nullable = false)
    private BigDecimal subTotal;

    // --- LIÊN KẾT IMEI/SERIAL ---
    // Quan hệ One-To-Many: Danh sách các máy vật lý (Serial/IMEI cụ thể) được xuất ra cho dòng này
    // (Lúc này bạn có thể bỏ comment ở file ProductItem.java)
    @OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ProductItem> productItems = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        calculateSubTotal();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        calculateSubTotal();
    }

    // Tự động tính toán thành tiền trước khi lưu vào DB để tránh sai sót
    private void calculateSubTotal() {
        if (this.unitPrice != null && this.quantity != null) {
            BigDecimal baseTotal = this.unitPrice.multiply(new BigDecimal(this.quantity));
            if (this.discountAmount != null) {
                this.subTotal = baseTotal.subtract(this.discountAmount);
            } else {
                this.subTotal = baseTotal;
            }
        }
    }
}