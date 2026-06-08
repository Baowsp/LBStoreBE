package com.group.lbstore.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Đại diện cho một sản phẩm đang được khuyến mãi,
 * kèm giá sau khi giảm và % giảm giá áp dụng.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotedProductResponse {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String brand;
    private String category;
    private String categorySlug;

    // Giá gốc của variant đầu tiên
    private BigDecimal originalPrice;
    // Giá sau khi áp dụng promotion
    private BigDecimal promotionalPrice;
    // % giảm giá (null nếu là giảm cứng)
    private BigDecimal discountPercentage;
    // Số tiền giảm cứng VNĐ (null nếu là giảm %)
    private BigDecimal fixedDiscountAmount;
    // "PERCENT" hoặc "FIXED"
    private String discountType;

    // Tên chương trình khuyến mãi
    private String promotionName;
    // ID chương trình khuyến mãi
    private Long promotionId;
    // Hiển thị ở trang chủ
    private Boolean showOnHomepage;

    private List<VariantInfo> variants;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class VariantInfo {
        private Long id;
        private String storage;
        private BigDecimal originalPrice;
        private BigDecimal discountedPrice;
        private BigDecimal promotionalPrice;
        private int stockQuantity;
        private String thumbnailUrl;
        private List<ColorInfo> variantColors;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ColorInfo {
        private Long id;
        private String color;
        private int stockQuantity;
        private String imageUrl;
    }
}
