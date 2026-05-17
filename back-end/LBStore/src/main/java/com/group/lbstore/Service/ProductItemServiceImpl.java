package com.group.lbstore.Service;

import com.group.lbstore.Model.ProductItem;
import com.group.lbstore.Service.ProductItemService;
import com.group.lbstore.Repository.ProductItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductItemServiceImpl implements ProductItemService {
    private final ProductItemRepository productItemRepository;

    public ProductItem createProductItem(ProductItem productItem) {
        return productItemRepository.save(productItem);
    }

    public List<ProductItem> getAllProductItems() {
        return productItemRepository.findAll();
    }

    public ProductItem getProductItemById(Long id) {
        return productItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Product Item not found"));
    }

    public ProductItem updateProductItem(Long id, ProductItem itemDetails) {
        ProductItem existingItem = getProductItemById(id);
        existingItem.setStatus(itemDetails.getStatus());
        // Other fields should be updated carefully
        return productItemRepository.save(existingItem);
    }
}