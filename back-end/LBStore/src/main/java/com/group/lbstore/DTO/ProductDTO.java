package com.group.lbstore.DTO;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String category;
    private String brandName;
    private List<ProductVariantDTO> variants;
}
