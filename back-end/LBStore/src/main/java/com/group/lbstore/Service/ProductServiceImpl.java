package com.group.lbstore.Service;

import com.group.lbstore.Model.Brand;
import com.group.lbstore.Model.Category;
import com.group.lbstore.Model.Product;
import com.group.lbstore.Repository.BrandRepository;
import com.group.lbstore.Repository.CategoryRepository;
import com.group.lbstore.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> searchByName(String query, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(query, pageable);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Optional<Product> findBySlug(String slug) {
        return productRepository.findBySlug(slug);
    }
    
    @Override
    public Page<Product> findByCategorySlug(String slug, Pageable pageable) {
        return productRepository.findByCategorySlug(slug, pageable);
    }

    @Override
    public Page<Product> findByCategoryName(String name, Pageable pageable) {
        return productRepository.findByCategoryNameContainingIgnoreCase(name, pageable);
    }

    @Override
    @Transactional
    public Product save(Product product) {
        // 1. Xử lý Thương hiệu (Brand)
        if (product.getBrand() != null && product.getBrand().getName() != null) {
            String brandName = product.getBrand().getName();
            Brand brand = brandRepository.findByName(brandName)
                    .orElseGet(() -> brandRepository.save(Brand.builder().name(brandName).build()));
            product.setBrand(brand);
        }

        // 1.1 Xử lý Danh mục (Category)
        if (product.getCategory() != null && product.getCategory().getName() != null) {
            String catName = product.getCategory().getName();
            Category category = categoryRepository.findByName(catName)
                    .orElseGet(() -> categoryRepository.save(Category.builder().name(catName).build()));
            product.setCategory(category);
        }

        // 2. Tạo Slug nếu chưa có
        if (product.getSlug() == null || product.getSlug().isEmpty()) {
            product.setSlug(toSlug(product.getName()));
        }

        // 3. Liên kết cha-con cho các biến thể (QUAN TRỌNG) VÀ tính toán tồn kho
        if (product.getVariants() != null) {
            int totalProductStock = 0;
            for (var variant : product.getVariants()) {
                variant.setProduct(product);
                
                int variantStock = 0;
                // Liên kết cha-con cho các màu sắc của biến thể
                if (variant.getVariantColors() != null && !variant.getVariantColors().isEmpty()) {
                    for (var color : variant.getVariantColors()) {
                        color.setProductVariant(variant);
                        variantStock += color.getStockQuantity();
                    }
                    // Cập nhật tồn kho biến thể bằng tổng tồn kho các màu sắc
                    variant.setStockQuantity(variantStock);
                } else {
                    // Nếu không có phần loại màu, lấy số lượng gốc của biến thể
                    variantStock = variant.getStockQuantity();
                }
                
                totalProductStock += variantStock;
            }
            // Cập nhật số lượng tổng của Product
            product.setStockQuantity(totalProductStock);
        }

        return productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    private String toSlug(String input) {
        if (input == null) return "";
        String nowhitespace = Pattern.compile("\\s+").matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = Pattern.compile("[^\\w-]").matcher(normalized).replaceAll("");
        return slug.toLowerCase();
    }
}