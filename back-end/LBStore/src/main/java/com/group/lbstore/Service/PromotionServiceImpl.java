package com.group.lbstore.Service;

import com.group.lbstore.DTO.PromotedProductResponse;
import com.group.lbstore.DTO.PromotionRequest;
import com.group.lbstore.DTO.PromotionResponse;
import com.group.lbstore.Model.Product;
import com.group.lbstore.Model.ProductVariant;
import com.group.lbstore.Model.Promotion;
import com.group.lbstore.Repository.ProductRepository;
import com.group.lbstore.Repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;

    // ── CRUD ───────────────────────────────────────────────────────────────────

    @Override
    public List<PromotionResponse> getAllPromotions() {
        return promotionRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public Optional<PromotionResponse> getPromotionById(Long id) {
        return promotionRepository.findById(id).map(this::toResponse);
    }

    @Override
    @Transactional
    public PromotionResponse createPromotion(PromotionRequest request) {
        // Validate không trùng thời gian
        validateNoOverlap(request.getProductIds(), -1L,
                request.getStartDate(), request.getEndDate());

        Promotion promotion = Promotion.builder()
                .name(request.getName())
                .description(request.getDescription())
                .discountPercentage(request.getDiscountPercentage())
                .fixedDiscountAmount(request.getFixedDiscountAmount())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .isActive(request.getIsActive() != null ? request.getIsActive() : false)
                .showOnHomepage(request.getShowOnHomepage() != null ? request.getShowOnHomepage() : true)
                .build();

        // Lưu trước để có ID
        Promotion saved = promotionRepository.save(promotion);

        // Gán promotion_id cho từng sản phẩm
        applyProducts(saved, request.getProductIds());

        return toResponse(saved);
    }

    @Override
    @Transactional
    public PromotionResponse updatePromotion(Long id, PromotionRequest request) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found: " + id));

        // Validate không trùng thời gian (loại trừ chính promotion này)
        validateNoOverlap(request.getProductIds(), id,
                request.getStartDate(), request.getEndDate());

        // Clear promotion_id của các sản phẩm cũ
        promotionRepository.clearProductsFromPromotion(id);

        promotion.setName(request.getName());
        promotion.setDescription(request.getDescription());
        promotion.setDiscountPercentage(request.getDiscountPercentage());
        promotion.setFixedDiscountAmount(request.getFixedDiscountAmount());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        if (request.getIsActive() != null) promotion.setIsActive(request.getIsActive());
        if (request.getShowOnHomepage() != null) promotion.setShowOnHomepage(request.getShowOnHomepage());

        Promotion saved = promotionRepository.save(promotion);

        // Gán promotion mới cho danh sách sản phẩm
        applyProducts(saved, request.getProductIds());

        return toResponse(saved);
    }

    @Override
    @Transactional
    public void deletePromotion(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found: " + id));

        // Clear FK trên sản phẩm trước khi xóa promotion
        promotionRepository.clearProductsFromPromotion(id);
        promotion.getCategories().clear();
        promotionRepository.delete(promotion);
    }

    @Override
    public List<PromotionResponse> getActivePromotions() {
        return promotionRepository.findActivePromotions(LocalDateTime.now())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotedProductResponse> getPromotedProducts() {
        List<Promotion> activePromotions = promotionRepository.findActivePromotions(LocalDateTime.now());

        // Giữ promotion lợi nhất (promotionalPrice thấp nhất) cho mỗi sản phẩm
        // (vì 1-N, mỗi product chỉ thuộc 1 promotion, nên map này thực ra chỉ có 1 entry / product)
        Map<Long, PromotedProductResponse> productMap = new LinkedHashMap<>();

        for (Promotion promo : activePromotions) {
            List<Product> promoProducts = promo.getProducts() != null ? promo.getProducts() : List.of();
            for (Product product : promoProducts) {
                PromotedProductResponse candidate = buildResponse(product, promo);
                productMap.compute(product.getId(), (k, existing) -> {
                    if (existing == null) return candidate;
                    return existing.getPromotionalPrice().compareTo(candidate.getPromotionalPrice()) <= 0
                            ? existing : candidate;
                });
            }
        }

        return new ArrayList<>(productMap.values());
    }

    // ── Validation ─────────────────────────────────────────────────────────────

    /**
     * Ném RuntimeException nếu bất kỳ productId nào đã có promotion khác
     * (không phải excludeId) với khoảng thời gian trùng.
     * Format lỗi: JSON array để frontend dễ parse.
     */
    private void validateNoOverlap(List<Long> productIds, Long excludeId,
                                    LocalDateTime start, LocalDateTime end) {
        if (productIds == null || productIds.isEmpty()) return;

        // Dùng StringBuilder tạo JSON array thủ công (tránh phụ thuộc thêm thư viện)
        List<String> jsonItems = new ArrayList<>();

        for (Long pid : productIds) {
            List<Promotion> overlapping = promotionRepository.findOverlappingForProduct(
                    pid, excludeId == null ? -1L : excludeId, start, end);
            if (!overlapping.isEmpty()) {
                String productName = productRepository.findById(pid)
                        .map(Product::getName).orElse("ID " + pid);

                for (Promotion pr : overlapping) {
                    String promoStart = pr.getStartDate() != null
                            ? pr.getStartDate().toString().replace("T", " ").substring(0, 16)
                            : "không giới hạn";
                    String promoEnd = pr.getEndDate() != null
                            ? pr.getEndDate().toString().replace("T", " ").substring(0, 16)
                            : "không giới hạn";

                    jsonItems.add(String.format(
                            "{\"product\":\"%s\",\"promotion\":\"%s\",\"start\":\"%s\",\"end\":\"%s\"}",
                            productName.replace("\"", "'"),
                            pr.getName().replace("\"", "'"),
                            promoStart,
                            promoEnd
                    ));
                }
            }
        }

        if (!jsonItems.isEmpty()) {
            throw new RuntimeException("[" + String.join(",", jsonItems) + "]");
        }
    }

    // ── Private helpers ────────────────────────────────────────────────────────

    /**
     * Gán promotion_id cho từng sản phẩm trong danh sách productIds.
     */
    private void applyProducts(Promotion promotion, List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) return;
        List<Product> products = productRepository.findAllById(productIds);
        for (Product p : products) {
            p.setPromotion(promotion);
        }
        productRepository.saveAll(products);
    }

    private PromotedProductResponse buildResponse(Product product, Promotion promo) {
        List<ProductVariant> variants = product.getVariants() != null
                ? product.getVariants() : List.of();
        ProductVariant first = variants.isEmpty() ? null : variants.get(0);

        BigDecimal originalPrice = first != null ? first.getOriginalPrice() : BigDecimal.ZERO;
        boolean isFixed = promo.getFixedDiscountAmount() != null
                && promo.getFixedDiscountAmount().compareTo(BigDecimal.ZERO) > 0;

        BigDecimal promotionalPrice;
        BigDecimal effectivePct;
        BigDecimal fixedAmt;
        String discountType;

        if (isFixed) {
            fixedAmt = promo.getFixedDiscountAmount();
            promotionalPrice = originalPrice.subtract(fixedAmt).max(BigDecimal.ZERO)
                    .setScale(0, RoundingMode.HALF_UP);
            effectivePct = originalPrice.compareTo(BigDecimal.ZERO) > 0
                    ? BigDecimal.valueOf(100).subtract(
                            promotionalPrice.multiply(BigDecimal.valueOf(100))
                                    .divide(originalPrice, 2, RoundingMode.HALF_UP))
                    : BigDecimal.ZERO;
            discountType = "FIXED";
        } else {
            BigDecimal pct = promo.getDiscountPercentage() != null
                    ? promo.getDiscountPercentage() : BigDecimal.ZERO;
            BigDecimal factor = BigDecimal.ONE.subtract(
                    pct.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
            promotionalPrice = originalPrice.multiply(factor).setScale(0, RoundingMode.HALF_UP);
            effectivePct = pct;
            fixedAmt = null;
            discountType = "PERCENT";
        }

        final BigDecimal fPromoPrice = promotionalPrice;
        final BigDecimal fPct = effectivePct;
        final boolean fIsFixed = isFixed;
        final BigDecimal fFixed = fixedAmt;

        List<PromotedProductResponse.VariantInfo> variantInfos = variants.stream().map(v -> {
            BigDecimal vPromo;
            if (fIsFixed) {
                vPromo = v.getOriginalPrice().subtract(fFixed).max(BigDecimal.ZERO)
                        .setScale(0, RoundingMode.HALF_UP);
            } else {
                BigDecimal factor = BigDecimal.ONE.subtract(
                        fPct.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
                vPromo = v.getOriginalPrice().multiply(factor).setScale(0, RoundingMode.HALF_UP);
            }
            List<PromotedProductResponse.ColorInfo> colors = v.getVariantColors() != null
                    ? v.getVariantColors().stream().map(c -> PromotedProductResponse.ColorInfo.builder()
                            .id(c.getId()).color(c.getColor())
                            .stockQuantity(c.getStockQuantity()).imageUrl(c.getImageUrl())
                            .build()).collect(Collectors.toList())
                    : List.of();
            return PromotedProductResponse.VariantInfo.builder()
                    .id(v.getId()).storage(v.getStorage())
                    .originalPrice(v.getOriginalPrice()).discountedPrice(v.getDiscountedPrice())
                    .promotionalPrice(vPromo).stockQuantity(v.getStockQuantity())
                    .thumbnailUrl(v.getThumbnailUrl()).variantColors(colors)
                    .build();
        }).collect(Collectors.toList());

        return PromotedProductResponse.builder()
                .id(product.getId()).name(product.getName()).slug(product.getSlug())
                .description(product.getDescription())
                .brand(product.getBrand() != null ? product.getBrand().getName() : null)
                .category(product.getCategory() != null ? product.getCategory().getName() : null)
                .categorySlug(product.getCategory() != null ? product.getCategory().getSlug() : null)
                .originalPrice(originalPrice).promotionalPrice(fPromoPrice)
                .discountPercentage(fIsFixed ? null : fPct)
                .fixedDiscountAmount(fIsFixed ? fFixed : null)
                .discountType(discountType)
                .promotionName(promo.getName()).promotionId(promo.getId())
                .showOnHomepage(promo.getShowOnHomepage())
                .variants(variantInfos)
                .build();
    }

    private PromotionResponse toResponse(Promotion promotion) {
        List<Product> products = promotion.getProducts() != null ? promotion.getProducts() : List.of();
        List<PromotionResponse.PromotionProductInfo> productInfos = products.stream()
                .map(p -> PromotionResponse.PromotionProductInfo.builder()
                        .id(p.getId()).name(p.getName()).slug(p.getSlug()).build())
                .collect(Collectors.toList());

        Set<com.group.lbstore.Model.Category> cats = promotion.getCategories();
        List<PromotionResponse.PromotionCategoryInfo> categoryInfos = cats != null
                ? cats.stream().map(c -> PromotionResponse.PromotionCategoryInfo.builder()
                        .id(c.getId()).name(c.getName()).slug(c.getSlug()).build())
                        .collect(Collectors.toList())
                : List.of();

        boolean isFixed = promotion.getFixedDiscountAmount() != null
                && promotion.getFixedDiscountAmount().compareTo(BigDecimal.ZERO) > 0;

        return PromotionResponse.builder()
                .id(promotion.getId()).name(promotion.getName())
                .description(promotion.getDescription())
                .discountPercentage(isFixed ? null : promotion.getDiscountPercentage())
                .fixedDiscountAmount(isFixed ? promotion.getFixedDiscountAmount() : null)
                .discountType(isFixed ? "FIXED" : "PERCENT")
                .startDate(promotion.getStartDate()).endDate(promotion.getEndDate())
                .isActive(promotion.getIsActive())
                .showOnHomepage(promotion.getShowOnHomepage())
                .createdAt(promotion.getCreatedAt()).updatedAt(promotion.getUpdatedAt())
                .products(productInfos).categories(categoryInfos)
                .build();
    }
}
