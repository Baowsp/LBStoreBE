package com.group.lbstore.Repository;

import com.group.lbstore.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySlug(String slug);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    @Query("SELECT p FROM Product p WHERE p.category.slug = :id OR p.category.name = :id")
    Page<Product> findByCategorySlug(@org.springframework.data.repository.query.Param("id") String id, Pageable pageable);

    Page<Product> findByCategoryNameContainingIgnoreCase(String name, Pageable pageable);
    
    @Override
    @Query(
        value = "SELECT DISTINCT p FROM Product p",
        countQuery = "SELECT COUNT(DISTINCT p) FROM Product p"
    )
    Page<Product> findAll(Pageable pageable);
}