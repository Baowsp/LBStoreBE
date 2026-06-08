package com.group.lbstore.Service;

import com.group.lbstore.DTO.PromotedProductResponse;
import com.group.lbstore.DTO.PromotionRequest;
import com.group.lbstore.DTO.PromotionResponse;
import com.group.lbstore.Model.Promotion;

import java.util.List;
import java.util.Optional;

public interface PromotionService {

    List<PromotionResponse> getAllPromotions();

    Optional<PromotionResponse> getPromotionById(Long id);

    PromotionResponse createPromotion(PromotionRequest request);

    PromotionResponse updatePromotion(Long id, PromotionRequest request);

    void deletePromotion(Long id);

    /**
     * Lấy các promotion đang hoạt động (isActive=true và trong thời hạn)
     */
    List<PromotionResponse> getActivePromotions();

    /**
     * Lấy danh sách sản phẩm đang được khuyến mãi (để hiển thị trên HomePage)
     */
    List<PromotedProductResponse> getPromotedProducts();
}
