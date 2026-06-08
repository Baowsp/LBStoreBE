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
public class PromotionRequest {
    private String name;
    private String description;
    private BigDecimal discountPercentage;
    private BigDecimal fixedDiscountAmount;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isActive;
    private Boolean showOnHomepage;
    // Danh sách ID sản phẩm áp dụng
    private List<Long> productIds;
    // Danh sách ID danh mục áp dụng
    private List<Long> categoryIds;
}
