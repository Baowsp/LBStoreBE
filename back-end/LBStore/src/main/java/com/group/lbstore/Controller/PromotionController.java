package com.group.lbstore.Controller;

import com.group.lbstore.DTO.PromotedProductResponse;
import com.group.lbstore.DTO.PromotionRequest;
import com.group.lbstore.DTO.PromotionResponse;
import com.group.lbstore.Service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    // ── Admin endpoints ─────────────────────────────────────────────────────

    /**
     * GET /api/promotions — Lấy tất cả promotions (Admin)
     */
    @GetMapping
    public ResponseEntity<List<PromotionResponse>> getAllPromotions() {
        return ResponseEntity.ok(promotionService.getAllPromotions());
    }

    /**
     * GET /api/promotions/{id} — Lấy chi tiết promotion
     */
    @GetMapping("/{id}")
    public ResponseEntity<PromotionResponse> getPromotionById(@PathVariable Long id) {
        return promotionService.getPromotionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/promotions — Tạo mới promotion
     */
    @PostMapping
    public ResponseEntity<PromotionResponse> createPromotion(@RequestBody PromotionRequest request) {
        return ResponseEntity.ok(promotionService.createPromotion(request));
    }

    /**
     * PUT /api/promotions/{id} — Cập nhật promotion
     */
    @PutMapping("/{id}")
    public ResponseEntity<PromotionResponse> updatePromotion(@PathVariable Long id,
                                                              @RequestBody PromotionRequest request) {
        return ResponseEntity.ok(promotionService.updatePromotion(id, request));
    }

    /**
     * DELETE /api/promotions/{id} — Xóa promotion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }

    // ── Public endpoints ────────────────────────────────────────────────────

    /**
     * GET /api/promotions/active — Lấy các promotion đang chạy (Public)
     */
    @GetMapping("/active")
    public ResponseEntity<List<PromotionResponse>> getActivePromotions() {
        return ResponseEntity.ok(promotionService.getActivePromotions());
    }

    /**
     * GET /api/promotions/products — Lấy sản phẩm đang được khuyến mãi (Public - dùng ở HomePage)
     */
    @GetMapping("/products")
    public ResponseEntity<List<PromotedProductResponse>> getPromotedProducts() {
        return ResponseEntity.ok(promotionService.getPromotedProducts());
    }
}
