package com.group.lbstore.DTO;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantDTO {
    private Long id;
    private String sku;
    private String storage;
    private BigDecimal originalPrice;
    private BigDecimal discountedPrice;
    private int stockQuantity;
    private String thumbnailUrl;
    private Long productId;
    private List<ProductVariantColorDTO> variantColors;
}
