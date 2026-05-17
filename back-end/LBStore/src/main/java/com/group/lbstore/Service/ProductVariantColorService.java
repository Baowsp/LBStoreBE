package com.group.lbstore.Service;

import com.group.lbstore.Model.ProductVariantColor;
import java.util.List;

public interface ProductVariantColorService {
    List<ProductVariantColor> getColorsByVariantId(Long variantId);
    ProductVariantColor getColorById(Long id);
    ProductVariantColor createColor(Long variantId, ProductVariantColor color);
    ProductVariantColor updateColor(Long id, ProductVariantColor colorDetails);
    void deleteColor(Long id);
}
