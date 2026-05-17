package com.group.lbstore.Model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "export_invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExportInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mã phiếu xuất kho (VD: PXK-20260305-001) - Rất quan trọng để kế toán đối soát
    @Column(name = "invoice_number", nullable = false, unique = true, length = 50)
    private String invoiceNumber;

    // Quan hệ Many-To-One: Nhân viên kho nào chịu trách nhiệm nhặt hàng và lập phiếu này?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_employee_id", nullable = false)
    private WarehouseEmployee warehouseEmployee;

    // Quan hệ Many-To-One: Quản lý nào đã duyệt cho phép xuất hàng? (Chống thất thoát)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager approvedBy;

    // --- LIÊN KẾT CHỨNG TỪ GỐC ---

    // Phiếu xuất này phục vụ cho Đơn hàng mua trực tiếp nào? (Nullable vì có thể là đơn Online)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    // Phiếu xuất này phục vụ cho Đơn hàng Online nào? (Nullable vì có thể là đơn Offline)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "online_order_id")
    private OnlineOrder onlineOrder;

    // --- TÀI CHÍNH & TRẠNG THÁI ---

    // Tổng giá trị vốn của lô hàng xuất đi (Dùng để tính lợi nhuận gộp sau này)
    @Column(name = "total_value", precision = 15, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal totalValue = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private ExportStatus status = ExportStatus.DRAFT;

    // Thời điểm hàng hóa thực sự được bàn giao cho Shipper hoặc Khách hàng
    @Column(name = "export_date")
    private LocalDateTime exportDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes; // Ghi chú (VD: Giao cho Shipper Nguyễn Văn A - Biển số 29X1-12345)

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

