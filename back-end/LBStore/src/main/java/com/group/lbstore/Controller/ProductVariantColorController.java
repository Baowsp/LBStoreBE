package com.group.lbstore.Controller;

import com.group.lbstore.Model.ProductVariantColor;
import com.group.lbstore.Service.ProductVariantColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints cho ProductVariantColor (màu sắc của từng biến thể sản phẩm).
 *
 * GET    /api/product-variants/{variantId}/colors        — lấy tất cả màu theo biến thể
 * GET    /api/product-variant-colors/{id}                — lấy 1 màu theo id
 * POST   /api/product-variants/{variantId}/colors        — thêm màu mới cho biến thể
 * PUT    /api/product-variant-colors/{id}                — cập nhật màu
 * DELETE /api/product-variant-colors/{id}                — xóa màu
 */
@RestController
@RequiredArgsConstructor
public class ProductVariantColorController {

    private final ProductVariantColorService colorService;

    // ----- GET all colors for a specific variant -----
    @GetMapping("/api/product-variants/{variantId}/colors")
    public ResponseEntity<List<ProductVariantColor>> getColorsByVariant(@PathVariable Long variantId) {
        return ResponseEntity.ok(colorService.getColorsByVariantId(variantId));
    }

    // ----- GET single color by id -----
    @GetMapping("/api/product-variant-colors/{id}")
    public ResponseEntity<ProductVariantColor> getColorById(@PathVariable Long id) {
        return ResponseEntity.ok(colorService.getColorById(id));
    }

    // ----- POST create a new color under a variant -----
    @PostMapping("/api/product-variants/{variantId}/colors")
    public ResponseEntity<ProductVariantColor> createColor(
            @PathVariable Long variantId,
            @RequestBody ProductVariantColor color) {
        return ResponseEntity.ok(colorService.createColor(variantId, color));
    }

    // ----- PUT update existing color -----
    @PutMapping("/api/product-variant-colors/{id}")
    public ResponseEntity<ProductVariantColor> updateColor(
            @PathVariable Long id,
            @RequestBody ProductVariantColor colorDetails) {
        return ResponseEntity.ok(colorService.updateColor(id, colorDetails));
    }

    // ----- DELETE color -----
    @DeleteMapping("/api/product-variant-colors/{id}")
    public ResponseEntity<Void> deleteColor(@PathVariable Long id) {
        colorService.deleteColor(id);
        return ResponseEntity.ok().build();
    }
}
