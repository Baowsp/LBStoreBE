package com.group.lbstore.Model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    // Khóa chính dùng UUID cực kỳ quan trọng cho Đơn hàng để bảo mật doanh số
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Mã đơn hàng hiển thị trên biên lai in cho khách (VD: ORD-20260304-XYZ)
    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;

    // Quan hệ Many-To-One: Ai là người mua?
    // nullable = true vì khách mua trực tiếp có thể không cần tạo hồ sơ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Builder.Default
    private List<OrderDetail> orderDetailList = new ArrayList<>();

    // Tên khách và SĐT tạm thời (Dành cho khách vãng lai không có Customer ID)
    @Column(name = "guest_name", length = 100)
    private String guestName;

    @Column(name = "guest_phone", length = 20)
    private String guestPhone;

    // QUAN TRỌNG: Nhân viên kinh doanh nào chốt đơn này tại cửa hàng?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_employee_id", nullable = false)
    private SalesEmployee salesEmployee;

    // --- TÀI CHÍNH ---

    // Tổng tiền gốc của các sản phẩm
    @Column(name = "total_amount", precision = 15, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    // Số tiền giảm giá (VD: Khách hàng Vàng được giảm 5%, hoặc nhân viên linh động giảm giá)
    @Column(name = "discount_amount", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    // Số tiền thực tế khách phải trả (totalAmount - discountAmount)
    @Column(name = "final_amount", precision = 15, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal finalAmount = BigDecimal.ZERO;

    // Phương thức thanh toán tại quầy
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 50)
    private PaymentMethod paymentMethod;

    // --- TRẠNG THÁI ---

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 50)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PAID; // Đa số mua tại cửa hàng là trả tiền ngay

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, length = 50)
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.COMPLETED; // Nhận máy trực tiếp nên hoàn thành luôn

    // --- KIỂM TOÁN ---

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

    protected void updateTotalAmount() {
        totalAmount = BigDecimal.ZERO;
        for (OrderDetail orderDetail : orderDetailList){
            totalAmount.add(orderDetail.getSubTotal());
        }
        finalAmount = totalAmount.subtract(discountAmount);
    }
}

