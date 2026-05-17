package com.group.lbstore.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantColorDTO {
    private Long id;
    private String color;
    private int stockQuantity;
    private String imageUrl;
    private Long productVariantId;
}
