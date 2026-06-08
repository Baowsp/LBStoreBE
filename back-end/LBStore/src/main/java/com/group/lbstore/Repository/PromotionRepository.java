package com.group.lbstore.Repository;

import com.group.lbstore.Model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    /**
     * Lấy tất cả promotions đang hoạt động (isActive=true và trong khoảng thời gian)
     */
    @Query("SELECT p FROM Promotion p WHERE p.isActive = true " +
           "AND (p.startDate IS NULL OR p.startDate <= :now) " +
           "AND (p.endDate IS NULL OR p.endDate >= :now)")
    List<Promotion> findActivePromotions(@Param("now") LocalDateTime now);

    /**
     * Lấy tất cả promotions (Admin)
     */
    List<Promotion> findAllByOrderByCreatedAtDesc();

    /**
     * Kiểm tra productId có đang được gắn vào một promotion khác (không phải excludeId)
     * có khoảng thời gian trùng không.
     * Với quan hệ 1-N, một sản phẩm chỉ được thuộc 1 promotion trong cùng khoảng thời gian.
     * Hai khoảng [a,b] và [c,d] TRÙNG khi a < d AND c < b.
     */
    @Query("SELECT pr FROM Promotion pr JOIN pr.products prod " +
           "WHERE prod.id = :productId " +
           "AND pr.id <> :excludeId " +
           "AND (pr.startDate IS NULL OR :endDate IS NULL OR pr.startDate < :endDate) " +
           "AND (pr.endDate IS NULL OR :startDate IS NULL OR pr.endDate > :startDate)")
    List<Promotion> findOverlappingForProduct(
            @Param("productId") Long productId,
            @Param("excludeId") Long excludeId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * Xoá FK promotion_id khỏi tất cả products của một promotion cụ thể.
     * Dùng trước khi update (clear old) hoặc delete promotion.
     */
    @Modifying
    @Query("UPDATE Product p SET p.promotion = NULL WHERE p.promotion.id = :promotionId")
    void clearProductsFromPromotion(@Param("promotionId") Long promotionId);
}
