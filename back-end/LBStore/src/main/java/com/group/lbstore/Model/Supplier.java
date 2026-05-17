package com.group.lbstore.Model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 200)
    private String name; // Tên công ty/nhà phân phối (VD: Công ty CP Thế Giới Số - Digiworld)

    @Column(name = "tax_code", unique = true, length = 50)
    private String taxCode; // Mã số thuế (Bắt buộc phải có để xuất/nhập hóa đơn đỏ VAT)

    @Column(name = "contact_person", length = 100)
    private String contactPerson; // Tên người liên hệ trực tiếp (Sales của bên nhà cung cấp)

    @Column(name = "phone_number", length = 20)
    private String phoneNumber; // Số điện thoại liên hệ

    @Column(name = "email", length = 150)
    private String email; // Email để gửi đơn đặt hàng (Purchase Order)

    @Column(name = "address", columnDefinition = "TEXT")
    private String address; // Địa chỉ trụ sở/kho của nhà cung cấp

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true; // Trạng thái hợp tác (True: Đang nhập hàng, False: Đã ngừng hợp tác)

    // Quan hệ One-To-Many: Một nhà cung cấp có thể có nhiều phiếu nhập kho (Import Receipt)
    // Bỏ comment khi bạn tạo Model ImportReceipt (Phiếu nhập kho)

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImportInvoice> importInvoiceList;

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