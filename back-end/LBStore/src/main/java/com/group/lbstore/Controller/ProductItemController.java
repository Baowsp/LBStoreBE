package com.group.lbstore.Controller;

import com.group.lbstore.Model.ProductItem;
import com.group.lbstore.Service.ProductItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product-items")
@RequiredArgsConstructor
public class ProductItemController {

    private final ProductItemService productItemService;

    @PostMapping
    public ResponseEntity<ProductItem> createProductItem(@RequestBody ProductItem productItem) {
        ProductItem created = productItemService.createProductItem(productItem);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductItem>> getAllProductItems() {
        List<ProductItem> items = productItemService.getAllProductItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductItem> getProductItemById(@PathVariable Long id) {
        ProductItem item = productItemService.getProductItemById(id);
        return ResponseEntity.ok(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductItem> updateProductItem(@PathVariable Long id, @RequestBody ProductItem itemDetails) {
        ProductItem updated = productItemService.updateProductItem(id, itemDetails);
        return ResponseEntity.ok(updated);
    }
}