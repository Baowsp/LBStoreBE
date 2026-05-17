package com.group.lbstore.Service;

import com.group.lbstore.Model.ProductVariant;
import com.group.lbstore.Model.ProductVariantColor;
import com.group.lbstore.Repository.ProductVariantColorRepository;
import com.group.lbstore.Repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantColorServiceImpl implements ProductVariantColorService {

    private final ProductVariantColorRepository colorRepository;
    private final ProductVariantRepository variantRepository;

    @Override
    public List<ProductVariantColor> getColorsByVariantId(Long variantId) {
        return colorRepository.findByProductVariantId(variantId);
    }

    @Override
    public ProductVariantColor getColorById(Long id) {
        return colorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductVariantColor not found with id: " + id));
    }

    @Override
    public ProductVariantColor createColor(Long variantId, ProductVariantColor color) {
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("ProductVariant not found with id: " + variantId));
        color.setProductVariant(variant);
        return colorRepository.save(color);
    }

    @Override
    public ProductVariantColor updateColor(Long id, ProductVariantColor colorDetails) {
        ProductVariantColor existing = getColorById(id);
        existing.setColor(colorDetails.getColor());
        existing.setStockQuantity(colorDetails.getStockQuantity());
        existing.setImageUrl(colorDetails.getImageUrl());
        return colorRepository.save(existing);
    }

    @Override
    public void deleteColor(Long id) {
        colorRepository.deleteById(id);
    }
}
