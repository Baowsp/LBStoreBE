package com.group.lbstore.Service;

import com.group.lbstore.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {
    Page<Product> findAll(Pageable pageable);
    
    Page<Product> searchByName(String query, Pageable pageable);
    
    Optional<Product> findById(Long id);
    
    Optional<Product> findBySlug(String slug);
    
    Page<Product> findByCategorySlug(String slug, Pageable pageable);
    
    Page<Product> findByCategoryName(String name, Pageable pageable);
    
    Product save(Product product);
    
    void delete(Long id);
}