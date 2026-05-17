package com.group.lbstore.Controller;

import com.group.lbstore.Model.Product;
import com.group.lbstore.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Hỗ trợ phân trang: /api/products?page=0&size=10
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.findAll(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(@RequestParam String q, Pageable pageable) {
        return ResponseEntity.ok(productService.searchByName(q, pageable));
    }

    @GetMapping("/category/{slug}")
    public ResponseEntity<Page<Product>> getProductsByCategorySlug(@PathVariable String slug, Pageable pageable) {
        return ResponseEntity.ok(productService.findByCategorySlug(slug, pageable));
    }

    @GetMapping("/category-name/{name}")
    public ResponseEntity<Page<Product>> getProductsByCategoryName(@PathVariable String name, Pageable pageable) {
        return ResponseEntity.ok(productService.findByCategoryName(name, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<Product> getProductBySlug(@PathVariable String slug) {
        Optional<Product> product = productService.findBySlug(slug);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        return ResponseEntity.ok(productService.save(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}