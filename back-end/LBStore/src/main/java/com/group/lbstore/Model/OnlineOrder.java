package com.group.lbstore.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "online_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnlineOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;

    // ======= CUSTOMER =======
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    // ======= SHIPPING =======
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_address_id")
    @JsonIgnore
    private Address shippingAddress;

    /** Địa chỉ giao hàng lưu dạng chuỗi (tự populate) */
    @Column(name = "shipping_address", columnDefinition = "TEXT")
    private String shippingAddressText;

    @Column(name = "shipping_name", length = 100)
    private String shippingName;

    @Column(name = "shipping_phone", length = 20)
    private String shippingPhone;

    @Column(name = "shipping_fee", precision = 15, scale = 2)
    private BigDecimal shippingFee;

    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;

    // ======= DELIVERY EMPLOYEE =======
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_employee_id")
    @JsonIgnore
    private DeliveryEmployee deliveryEmployee;

    // ======= AMOUNT =======
    @Column(name = "total_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "final_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal finalAmount;

    // ======= STATUS =======
    /** Sử dụng cột status (OnlineOrderStatus enum) */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private OnlineOrderStatus status;

    /** Cột order_status (nếu DB có cả 2 cột) */
    @Column(name = "order_status", length = 50)
    private String orderStatus;

    // ======= PAYMENT =======
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "payment_status", length = 50)
    private String paymentStatus;

    @Column(name = "payment_transaction_id", length = 255)
    private String paymentTransactionId;

    // ======= MISC =======
    @Column(name = "voucher_code", length = 50)
    private String voucherCode;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // ======= ORDER DETAILS =======
    @OneToMany(mappedBy = "onlineOrder", cascade = CascadeType.ALL)
    @Builder.Default
    private List<OnlineOrderDetail> onlineOrderDetails = new ArrayList<>();

    // ======= TIMESTAMPS =======
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.orderStatus == null) this.orderStatus = "PENDING";
        if (this.paymentStatus == null) this.paymentStatus = "UNPAID";
        if (this.discountAmount == null) this.discountAmount = BigDecimal.ZERO;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
