package com.group.lbstore.Model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "warehouse_employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ One-To-One: Liên kết với hồ sơ Employee gốc
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false, unique = true)
    private Employee employee;

    // Mã kho mà nhân viên này đang làm việc (VD: KHO-HCM-01)
    // Nếu hệ thống lớn, bạn có thể tạo hẳn 1 Model Warehouse và dùng @ManyToOne ở đây
    @Column(name = "warehouse_code", nullable = false, length = 50)
    private String warehouseCode;

    // Vai trò cụ thể trong kho
    @Enumerated(EnumType.STRING)
    @Column(name = "warehouse_role", nullable = false, length = 50)
    private WarehouseRole warehouseRole;

    // Ca làm việc cố định
    @Enumerated(EnumType.STRING)
    @Column(name = "work_shift", nullable = false, length = 30)
    private WorkShift workShift;

    // Bằng cấp hoặc chứng chỉ an toàn (VD: PCCC, Chứng chỉ lái xe nâng)
    @Column(name = "safety_certification", length = 100)
    private String safetyCertification;

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

