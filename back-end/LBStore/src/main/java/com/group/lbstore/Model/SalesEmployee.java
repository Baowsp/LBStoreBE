package com.group.lbstore.Model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales_employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ One-To-One: Liên kết với hồ sơ Employee gốc
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false, unique = true)
    private Employee employee;

    // Tỷ lệ hoa hồng (VD: 1.50 cho 1.5% trên mỗi đơn hàng thành công)
    @Column(name = "commission_rate", precision = 5, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal commissionRate = BigDecimal.ZERO;

    // Chỉ tiêu doanh số (KPI) trong tháng (VD: 500,000,000 VNĐ)
    @Column(name = "monthly_sales_target", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal monthlySalesTarget = BigDecimal.ZERO;

    // Doanh số thực tế đã đạt được trong tháng hiện tại
    @Column(name = "current_month_sales", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal currentMonthSales = BigDecimal.ZERO;

    // Kênh bán hàng mà nhân viên này phụ trách
    @Enumerated(EnumType.STRING)
    @Column(name = "sales_channel", nullable = false, length = 50)
    private SalesChannel salesChannel;

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

