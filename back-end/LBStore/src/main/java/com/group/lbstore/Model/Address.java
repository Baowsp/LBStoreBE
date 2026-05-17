package com.group.lbstore.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ Many-To-One: Nhiều địa chỉ thuộc về 1 khách hàng
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Customer customer;

    // --- THÔNG TIN NGƯỜI NHẬN ---
    // Khách hàng A có thể thêm địa chỉ để giao máy cho bạn bè B, 
    // nên tên và SĐT ở đây có thể khác với tên/SĐT trong bảng Customer
    @Column(name = "receiver_name", nullable = false, length = 100)
    private String receiverName;

    @Column(name = "receiver_phone", nullable = false, length = 20)
    private String receiverPhone;

    // --- PHÂN CẤP HÀNH CHÍNH (Dùng để tính phí ship qua API GHTK/Viettel Post) ---
    
    @Column(name = "province", nullable = false, length = 100)
    private String province; // Tỉnh / Thành phố trực thuộc TW (VD: Hà Nội, TP.HCM)

    @Column(name = "district", nullable = false, length = 100)
    private String district; // Quận / Huyện (VD: Cầu Giấy, Quận 1)

    @Column(name = "ward", nullable = false, length = 100)
    private String ward; // Phường / Xã (VD: Dịch Vọng, Phường Bến Nghé)

    @Column(name = "street_address", nullable = false, length = 255)
    private String streetAddress; // Số nhà, tên đường, ngõ ngách chi tiết

    // --- THUỘC TÍNH BỔ SUNG ---

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type", length = 30)
    @Builder.Default
    private AddressType addressType = AddressType.HOME; // Loại địa chỉ để icon hiển thị cho đẹp

    @Column(name = "is_default", nullable = false)
    @Builder.Default
    private boolean isDefault = false; // Đánh dấu đây là địa chỉ mặc định khi vào trang Thanh toán

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

