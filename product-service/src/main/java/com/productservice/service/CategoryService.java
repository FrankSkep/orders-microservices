package com.productservice.service;

import com.productservice.entity.Category;
import com.productservice.exception.CategoryNotFoundException;
import com.productservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category createCategory(String categoryName) {
        if (categoryRepository.existsByName(categoryName)) {
            throw new CategoryNotFoundException("Category already exists: " + categoryName);
        }
        Category category = new Category(categoryName);
        return categoryRepository.save(category);
    }

    public Category getCategoryByName(String categoryName) {
        return categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + categoryName));
    }

    public void deleteCategory(String categoryName) {
        Category category = getCategoryByName(categoryName);
        categoryRepository.delete(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
