package com.group.lbstore.Model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ One-To-One: Gắn hồ sơ Shipper này với 1 hồ sơ Employee gốc
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false, unique = true)
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false, length = 30)
    private VehicleType vehicleType; // Loại phương tiện (Xe máy, Xe tải nhỏ)

    @Column(name = "license_plate", nullable = false, length = 20)
    private String licensePlate; // Biển số xe (Rất quan trọng để đối chiếu khi giao hàng giá trị cao)

    @Column(name = "driving_license", nullable = false, length = 50)
    private String drivingLicense; // Số bằng lái xe

    // --- TRACKING & STATUS ---

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private DeliveryStatus status = DeliveryStatus.OFFLINE; // Trạng thái hiện tại của Shipper

    @Column(name = "current_latitude")
    private Double currentLatitude; // Vĩ độ GPS hiện tại

    @Column(name = "current_longitude")
    private Double currentLongitude; // Kinh độ GPS hiện tại

    // --- THỐNG KÊ HIỆU SUẤT ---

    @Column(name = "successful_deliveries", nullable = false)
    @Builder.Default
    private Integer successfulDeliveries = 0; // Số đơn đã giao thành công

    @Column(name = "average_rating")
    @Builder.Default
    private Float averageRating = 5.0f; // Điểm đánh giá trung bình từ khách hàng (1.0 - 5.0)

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

