package com.group.lbstore.Model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "online_order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnlineOrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ Many-To-One: Dòng chi tiết này thuộc về Đơn hàng Online nào?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "online_order_id", nullable = false)
    @JsonIgnore
    private OnlineOrder onlineOrder;

    // Quan hệ Many-To-One: Khách đặt mua màu sắc + dung lượng cụ thể nào?
    // VD: iPhone 15 Pro Max 256GB - Titan Đen
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_color_id", nullable = false)
    private ProductVariantColor variantColor;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    // Đơn giá của sản phẩm tại thời điểm khách bấm "Đặt hàng"
    @Column(name = "unit_price", precision = 15, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    // Tiền giảm giá riêng cho dòng này (nếu có Flash Sale)
    @Column(name = "discount_amount", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    // Thành tiền = (quantity * unitPrice) - discountAmount
    @Column(name = "sub_total", precision = 15, scale = 2, nullable = false)
    private BigDecimal subTotal;

    // --- LIÊN KẾT IMEI/SERIAL KHI ĐÓNG GÓI ---

    // Quan hệ One-To-Many: Danh sách các thiết bị vật lý sẽ được giao cho dòng này
    // Cần mở file ProductItem.java và thêm cột tham chiếu đến OnlineOrderDetail
    @OneToMany(mappedBy = "onlineOrderDetail", cascade = CascadeType.ALL)
    @Builder.Default
    @JsonIgnore
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

    @JsonProperty("variant")
    public Map<String, Object> getVariantDetails() {
        Map<String, Object> map = new HashMap<>();
        if (this.variantColor != null) {
            map.put("color", this.variantColor.getColor());
            map.put("imageURL", this.variantColor.getImageUrl());
            if (this.variantColor.getProductVariant() != null) {
                map.put("storage", this.variantColor.getProductVariant().getStorage());
                if (this.variantColor.getProductVariant().getProduct() != null) {
                    Map<String, Object> productObj = new HashMap<>();
                    productObj.put("name", this.variantColor.getProductVariant().getProduct().getName());
                    map.put("product", productObj);
                }
            }
        }
        return map;
    }
}