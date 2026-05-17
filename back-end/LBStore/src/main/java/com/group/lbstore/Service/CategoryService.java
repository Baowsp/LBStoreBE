package com.group.lbstore.Service;

import com.group.lbstore.Model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findAll();
    Optional<Category> findById(Long id);
    Optional<Category> findByName(String name);
    Optional<Category> findBySlug(String slug);
    Category save(Category category);
    void delete(Long id);
}