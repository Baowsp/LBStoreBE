package com.group.lbstore.Model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "banners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tiêu đề của Banner (Dùng để Admin dễ quản lý và làm thẻ "alt" cho SEO)
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    // Đường dẫn ảnh (URL của ảnh sau khi upload lên S3 hoặc thư mục tĩnh)
    @Column(name = "image_url", nullable = false, columnDefinition = "TEXT")
    private String imageUrl;

    @Transient
    @JsonIgnore
    private MultipartFile imageFile;

    // Link đích khi khách hàng click vào tấm ảnh này (VD: /khuyen-mai/iphone-15)
    @Column(name = "target_url", nullable = false, columnDefinition = "TEXT")
    private String targetUrl;

    // Vị trí hiển thị của Banner trên Website / App
    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = false, length = 50)
    private BannerPosition position;

    // Thể loại banner, dành riêng cho CATEGORY_HEADER (VD: "laptop", "dien-thoai")
    @Column(name = "category_slug", length = 100)
    private String categorySlug;

    // Thứ tự hiển thị nếu có nhiều Banner ở cùng 1 vị trí (VD: Slider chạy từ 1 -> 5)
    @Column(name = "display_order", nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;

    // --- TỰ ĐỘNG HÓA MARKETING ---

    // Ngày giờ bắt đầu chiến dịch (Banner sẽ tự động hiện lên web khi đến giờ)
    @Column(name = "start_date")
    private LocalDateTime startDate;

    // Ngày giờ kết thúc chiến dịch (Banner tự động ẩn đi, không lo Admin quên tắt)
    @Column(name = "end_date")
    private LocalDateTime endDate;

    // Công tắc bật/tắt thủ công (Ghi đè lên cả thời gian nếu Admin muốn dừng khẩn cấp)
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;

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

