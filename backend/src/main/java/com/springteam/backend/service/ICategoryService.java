package com.springteam.backend.service;

import com.springteam.backend.dto.ProductCategoryDto;
import com.springteam.backend.dto.ProductCategoryResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryService {
    void insertNewCategory(ProductCategoryDto categoryDto);
    void updateExistedCategory(ProductCategoryDto categoryDto);
    void deleteCategory(Long id);
    List<ProductCategoryDto> getAllCategories();
    ProductCategoryResponse getAllCategories(Pageable pageable);
    ProductCategoryResponse getAllCategoriesByName(String name, Pageable pageable);
}
