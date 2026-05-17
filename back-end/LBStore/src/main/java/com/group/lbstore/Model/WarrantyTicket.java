package com.group.lbstore.Model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "warranty_tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarrantyTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mã phiếu bảo hành để in cho khách cầm về (VD: BH-20260305-XYZ)
    @Column(name = "ticket_number", nullable = false, unique = true, length = 50)
    private String ticketNumber;

    // QUAN TRỌNG NHẤT: Bắt buộc phải liên kết với 1 thiết bị vật lý cụ thể (IMEI/Serial)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_item_id", nullable = false)
    private ProductItem productItem;

    // Khách hàng nào mang máy đi bảo hành?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Nhân viên kỹ thuật nào tiếp nhận và xử lý ca này?
    // (Có thể liên kết với bảng Employee chung, hoặc bạn tạo thêm TechnicalEmployee tương tự như SalesEmployee)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technical_employee_id", nullable = false)
    private Employee technicalEmployee;

    // --- THÔNG TIN LỖI & SỬA CHỮA ---

    // Lời khai của khách hàng (VD: "Máy đang dùng tự nhiên sập nguồn, sạc không vào")
    @Column(name = "issue_description", columnDefinition = "TEXT", nullable = false)
    private String issueDescription;

    // Ghi chú của thợ kỹ thuật sau khi bung máy kiểm tra (VD: "Chết IC nguồn, móp góc do rơi vỡ")
    @Column(name = "technical_notes", columnDefinition = "TEXT")
    private String technicalNotes;

    // Trạng thái của tiến trình bảo hành
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    @Builder.Default
    private WarrantyStatus status = WarrantyStatus.RECEIVED;

    // Đánh dấu xem lỗi này có được hãng chấp nhận bảo hành miễn phí không?
    // (Nếu khách làm rơi vỡ vào nước -> false -> Tính phí sửa dịch vụ)
    @Column(name = "is_covered_by_warranty", nullable = false)
    @Builder.Default
    private boolean isCoveredByWarranty = true;

    // Chi phí sửa chữa (Nếu isCoveredByWarranty = true thì chi phí này = 0)
    @Column(name = "repair_cost", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal repairCost = BigDecimal.ZERO;

    // --- THỜI GIAN THEO DÕI ---

    // Ngày nhận máy của khách
    @Column(name = "receive_date", nullable = false)
    private LocalDateTime receiveDate;

    // Ngày hẹn trả máy dự kiến
    @Column(name = "expected_return_date")
    private LocalDateTime expectedReturnDate;

    // Ngày thực tế khách đến lấy máy về
    @Column(name = "actual_return_date")
    private LocalDateTime actualReturnDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.receiveDate == null) {
            this.receiveDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

