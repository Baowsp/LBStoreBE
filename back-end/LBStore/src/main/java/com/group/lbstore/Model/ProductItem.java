package com.group.lbstore.Model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ Many-To-One: Nhiều thiết bị vật lý thuộc về 1 màu sắc cụ thể
    // VD: 50 chiếc máy này đều là bản "iPhone 15 Pro Max - Titan - 256GB - Đen"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_color_id", nullable = false)
    private ProductVariantColor variantColor;

    // QUAN TRỌNG NHẤT: Mã Serial (Laptop) hoặc IMEI (Điện thoại)
    // Bắt buộc phải unique (không bao giờ có 2 máy trùng IMEI)
    @Column(name = "serial_number", nullable = false, unique = true, length = 100)
    private String serialNumber; 

    // Trạng thái của máy vật lý này
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private ItemStatus status = ItemStatus.IN_STOCK; 

    // --- LIÊN KẾT BẢO HÀNH & ĐƠN HÀNG (Sẽ dùng sau) ---

    // Thời hạn bảo hành của riêng máy này (Kích hoạt khi khách mua thành công)
    @Column(name = "warranty_end_date")
    private LocalDateTime warrantyEndDate;

    // Quan hệ Many-To-One: Chiếc máy này đã được bán trong đơn hàng/chi tiết đơn hàng nào?
    // (Sẽ bỏ comment khi bạn tạo Model OrderDetail)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "online_order_detail_id")
    private OnlineOrderDetail onlineOrderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    private ImportInvoiceDetail importInvoiceDetail;

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

