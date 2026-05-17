package com.group.lbstore.Repository;

import com.group.lbstore.Model.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
    // Tìm sản phẩm cụ thể qua Serial Number
    Optional<ProductItem> findBySerialNumber(String serialNumber);
}