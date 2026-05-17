package com.group.lbstore.Model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "import_invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mã phiếu nhập (VD: PNK-20260305-APP) - Thường lấy theo số hóa đơn đỏ của nhà cung cấp
    @Column(name = "invoice_number", nullable = false, unique = true, length = 50)
    private String invoiceNumber;

    // Quan hệ Many-To-One: Lô hàng này nhập từ Nhà cung cấp nào? (VD: Digiworld, FPT Synnex)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    // Quan hệ Many-To-One: Nhân viên kho nào chịu trách nhiệm kiểm đếm và lập phiếu?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_employee_id", nullable = false)
    private WarehouseEmployee warehouseEmployee;

    // Quan hệ Many-To-One: Quản lý / Kế toán trưởng nào duyệt chi tiền nhập lô hàng này?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager approvedBy;

    // --- TÀI CHÍNH & TRẠNG THÁI ---

    // Tổng tiền phải thanh toán cho nhà cung cấp (Giá vốn nhập hàng)
    @Column(name = "total_amount", precision = 15, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 30)
    @Builder.Default
    private ImportPaymentStatus paymentStatus = ImportPaymentStatus.UNPAID;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private ImportStatus status = ImportStatus.DRAFT;

    // Ngày giờ lô hàng thực tế cập bến kho của bạn
    @Column(name = "import_date")
    private LocalDateTime importDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes; // Ghi chú (VD: Lô hàng iPhone 15 bị móp 2 hộp ngoài cùng)

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "importInvoice", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ImportInvoiceDetail> importInvoiceDetails = new ArrayList<>();

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

