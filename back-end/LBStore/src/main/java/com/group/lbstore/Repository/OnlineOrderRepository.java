package com.group.lbstore.Repository;

import com.group.lbstore.Model.OnlineOrder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface OnlineOrderRepository extends JpaRepository<OnlineOrder, UUID> {
    
    @EntityGraph(attributePaths = {
        "onlineOrderDetails", 
        "onlineOrderDetails.variantColor", 
        "onlineOrderDetails.variantColor.productVariant",
        "onlineOrderDetails.variantColor.productVariant.product"
    })
    Optional<OnlineOrder> findById(UUID id);

    @EntityGraph(attributePaths = {
        "onlineOrderDetails", 
        "onlineOrderDetails.variantColor", 
        "onlineOrderDetails.variantColor.productVariant",
        "onlineOrderDetails.variantColor.productVariant.product"
    })
    List<OnlineOrder> findByCustomerId(Long customerId);

    OnlineOrder getOnlineOrdersByOrderNumber(String orderNumber);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END " +
           "FROM OnlineOrder o " +
           "JOIN o.onlineOrderDetails d " +
           "JOIN d.variantColor pvc " +
           "JOIN pvc.productVariant pv " +
           "WHERE o.customer.user.id = :userId AND pv.product.id = :productId " +
           "AND o.status != 'CANCELLED'")
    boolean hasUserPurchasedProduct(@Param("userId") UUID userId, @Param("productId") Long productId);
}