package com.group.lbstore.Model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "managers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ One-To-One: Liên kết với hồ sơ Employee gốc
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false, unique = true)
    private Employee employee;

    // Cấp bậc quản lý (VD: Quản lý cửa hàng, Giám đốc vùng)
    @Enumerated(EnumType.STRING)
    @Column(name = "management_level", nullable = false, length = 50)
    private ManagementLevel managementLevel;

    // Mã chi nhánh hoặc cửa hàng mà người này đang quản lý (VD: HN-STORE-01)
    @Column(name = "managed_branch_code", length = 50)
    private String managedBranchCode;

    // QUAN TRỌNG: Hạn mức giảm giá tối đa mà Quản lý này được phép phê duyệt cho 1 đơn hàng
    // VD: Sale chỉ được giảm 5%, nhưng Manager được quyền duyệt giảm tối đa 15% (Lưu số 15.00)
    @Column(name = "discount_approval_limit", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal discountApprovalLimit = BigDecimal.ZERO;

    // Phụ cấp trách nhiệm quản lý (Cộng thêm vào Base Salary của bảng Employee)
    @Column(name = "management_allowance", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal managementAllowance = BigDecimal.ZERO;

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

