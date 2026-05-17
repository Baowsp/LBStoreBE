package com.group.lbstore.Controller;

import com.group.lbstore.Model.ProductVariant;
import com.group.lbstore.Service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-variants")
@RequiredArgsConstructor
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    // GET /api/product-variants/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ProductVariant> getVariantById(@PathVariable Long id) {
        return ResponseEntity.ok(productVariantService.getVariantById(id));
    }

    // GET /api/product-variants?productId=1
    @GetMapping
    public ResponseEntity<List<ProductVariant>> getVariantsByProductId(@RequestParam Long productId) {
        return ResponseEntity.ok(productVariantService.getVariantsByProductId(productId));
    }

    // POST /api/product-variants
    @PostMapping
    public ResponseEntity<ProductVariant> createVariant(@RequestBody ProductVariant productVariant) {
        return ResponseEntity.ok(productVariantService.createProductVariant(productVariant));
    }

    // PUT /api/product-variants/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ProductVariant> updateVariant(
            @PathVariable Long id,
            @RequestBody ProductVariant productVariant) {
        return ResponseEntity.ok(productVariantService.updateVariant(id, productVariant));
    }

    // DELETE /api/product-variants/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVariant(@PathVariable Long id) {
        productVariantService.deleteVariant(id);
        return ResponseEntity.ok().build();
    }
}