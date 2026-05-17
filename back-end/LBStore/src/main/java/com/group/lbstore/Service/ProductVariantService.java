package com.group.lbstore.Service;

import com.group.lbstore.Model.ProductVariant;
import java.util.List;
import java.util.Optional;

public interface ProductVariantService {
    ProductVariant createProductVariant(ProductVariant variant);
    List<ProductVariant> getVariantsByProductId(Long productId);
    ProductVariant getVariantById(Long id);
    ProductVariant updateVariant(Long id, ProductVariant variantDetails);
    void deleteVariant(Long id);
    void updateStock(String sku, int quantityChange);
}