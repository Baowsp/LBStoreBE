package com.group.lbstore.Service;
import com.group.lbstore.Model.ProductItem;
import java.util.List;
public interface ProductItemService {
    ProductItem createProductItem(ProductItem productItem);
    List<ProductItem> getAllProductItems();
    ProductItem getProductItemById(Long id);
    ProductItem updateProductItem(Long id, ProductItem itemDetails);
}