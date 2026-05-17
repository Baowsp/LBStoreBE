package com.group.lbstore.Service;

import com.group.lbstore.Model.Category;
import com.group.lbstore.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.text.Normalizer;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @jakarta.transaction.Transactional
    public List<Category> findAll() {
        List<Category> categories = categoryRepository.findAll();
        // Một lần chạy duy nhất: Tự động điền slug nếu còn thiếu
        categories.forEach(c -> {
            if (c.getSlug() == null || c.getSlug().isEmpty()) {
                c.setSlug(toSlug(c.getName()));
                categoryRepository.save(c);
            }
        });
        return categories;
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Optional<Category> findBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }

    @Override
    public Category save(Category category) {
        if (category.getSlug() == null || category.getSlug().isEmpty()) {
            category.setSlug(toSlug(category.getName()));
        }
        return categoryRepository.save(category);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    private String toSlug(String input) {
        if (input == null) return "";
        String nowhitespace = Pattern.compile("\\s+").matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = Pattern.compile("[^\\w-]").matcher(normalized).replaceAll("");
        return slug.toLowerCase();
    }
}
