package com.group.lbstore.Model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "import_invoice_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportInvoiceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ Many-To-One: Dòng chi tiết này thuộc về Phiếu nhập kho nào?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "import_invoice_id", nullable = false)
    private ImportInvoice importInvoice;

    // Quan hệ Many-To-One: Nhập màu sắc + dung lượng cụ thể nào vào kho?
    // VD: MacBook Air M2 8GB 256GB - Màu Bạc
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_color_id", nullable = false)
    private ProductVariantColor variantColor;

    // Số lượng nhập của biến thể này (VD: 50 chiếc)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    // QUAN TRỌNG: Giá vốn nhập vào của 1 sản phẩm tại thời điểm này
    @Column(name = "unit_price", precision = 15, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    // Thành tiền của dòng nhập này = quantity * unitPrice
    @Column(name = "sub_total", precision = 15, scale = 2, nullable = false)
    private BigDecimal subTotal;

    // --- LIÊN KẾT KHAI SINH IMEI/SERIAL ---
    // Quan hệ One-To-Many: Danh sách các máy vật lý (IMEI cụ thể) được sinh ra từ lần nhập này
    // (Bạn cần mở file ProductItem.java và thêm: @ManyToOne private ImportInvoiceDetail importInvoiceDetail;)
    @OneToMany(mappedBy = "importInvoiceDetail", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ProductItem> productItems = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        calculateSubTotal();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        calculateSubTotal();
    }

    // Tự động tính toán thành tiền nhập để đảm bảo số liệu chính xác
    private void calculateSubTotal() {
        if (this.unitPrice != null && this.quantity != null) {
            this.subTotal = this.unitPrice.multiply(new BigDecimal(this.quantity));
        }
    }
}