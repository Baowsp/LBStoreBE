package com.group.lbstore.Model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ One-To-One: Gắn kết hồ sơ nhân viên này với 1 tài khoản đăng nhập
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private User user;

    @Column(name = "employee_code", nullable = false, unique = true, length = 20)
    private String employeeCode; // Mã nhân viên (VD: EMP-1004)

    @Enumerated(EnumType.STRING)
    @Column(name = "department", nullable = false, length = 50)
    private Department department; // Phòng ban làm việc

    @Column(name = "position", nullable = false, length = 100)
    private String position; // Chức vụ (VD: Trưởng ca, Nhân viên tư vấn, Kỹ thuật viên)

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate; // Ngày bắt đầu làm việc

    // Sử dụng BigDecimal cho tiền tệ để tránh sai số thập phân của double/float
    @Column(name = "base_salary", precision = 15, scale = 2)
    private BigDecimal baseSalary; // Lương cơ bản

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true; // Trạng thái làm việc (True: Đang làm, False: Đã nghỉ việc)

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

