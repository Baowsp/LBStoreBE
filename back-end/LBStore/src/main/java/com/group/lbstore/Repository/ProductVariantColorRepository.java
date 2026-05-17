package com.group.lbstore.Repository;

import com.group.lbstore.Model.ProductVariantColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantColorRepository extends JpaRepository<ProductVariantColor, Long> {
    List<ProductVariantColor> findByProductVariantId(Long productVariantId);
}
