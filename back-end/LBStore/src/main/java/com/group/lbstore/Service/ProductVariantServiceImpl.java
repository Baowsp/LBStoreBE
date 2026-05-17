package com.group.lbstore.Service;

import com.group.lbstore.Model.ProductVariant;
import com.group.lbstore.Repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository variantRepository;

    @Override
    public ProductVariant createProductVariant(ProductVariant variant) {
        // TODO: Check for SKU uniqueness
        return variantRepository.save(variant);
    }

    @Override
    public List<ProductVariant> getVariantsByProductId(Long productId) {
        return variantRepository.findByProductId(productId);
    }

    @Override
    public ProductVariant getVariantById(Long id) {
        return variantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Variant not found with id: " + id));
    }

    @Override
    public ProductVariant updateVariant(Long id, ProductVariant variantDetails) {
        ProductVariant existingVariant = getVariantById(id);
        existingVariant.setSku(variantDetails.getSku());
        existingVariant.setStorage(variantDetails.getStorage());
        existingVariant.setOriginalPrice(variantDetails.getOriginalPrice());
        existingVariant.setDiscountedPrice(variantDetails.getDiscountedPrice());
        existingVariant.setStockQuantity(variantDetails.getStockQuantity());
        existingVariant.setThumbnailUrl(variantDetails.getThumbnailUrl());
        return variantRepository.save(existingVariant);
    }

    @Override
    public void deleteVariant(Long id) {
        variantRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateStock(String sku, int quantityChange) {
        ProductVariant variant = variantRepository.findBySku(sku)
                .orElseThrow(() -> new RuntimeException("Variant with SKU " + sku + " not found."));
        int newStock = variant.getStockQuantity() + quantityChange;
        if (newStock < 0) {
            throw new RuntimeException("Not enough stock for SKU " + sku);
        }
        variant.setStockQuantity(newStock);
        variantRepository.save(variant);
    }
}