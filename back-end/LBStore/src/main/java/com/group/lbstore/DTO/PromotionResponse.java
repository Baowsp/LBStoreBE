package com.group.lbstore.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionResponse {
    private Long id;
    private String name;
    private String description;
    // % giảm giá áp dụng (null nếu là giảm cứng)
    private BigDecimal discountPercentage;
    // Số tiền giảm cứng (null nếu là giảm %)
    private BigDecimal fixedDiscountAmount;
    // "PERCENT" hoặc "FIXED"
    private String discountType;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isActive;
    private Boolean showOnHomepage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Sản phẩm áp dụng trực tiếp
    private List<PromotionProductInfo> products;
    // Danh mục áp dụng
    private List<PromotionCategoryInfo> categories;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PromotionProductInfo {
        private Long id;
        private String name;
        private String slug;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PromotionCategoryInfo {
        private Long id;
        private String name;
        private String slug;
    }
}
