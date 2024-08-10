package com.springteam.backend.service;

import com.springteam.backend.dto.ProductCategoryDto;
import com.springteam.backend.dto.ProductCategoryResponse;
import com.springteam.backend.entity.ProductCategory;
import com.springteam.backend.exception.CategoryNotFoundException;
import com.springteam.backend.exception.EmptyCategoryException;
import com.springteam.backend.repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {
    private ICategoryRepository repository;

    @Autowired
    public CategoryServiceImpl(ICategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void insertNewCategory(ProductCategoryDto categoryDto) {

        if (categoryDto == null || categoryDto.getName().trim().equals(""))
            throw new EmptyCategoryException("Thông tin danh mục không được để trống");

        ProductCategory productCategory = ProductCategory.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .description(categoryDto.getDescription())
                .build();
        repository.save(productCategory);
    }

    @Override
    public void updateExistedCategory(ProductCategoryDto categoryDto) {
        if (categoryDto == null || categoryDto.getName().trim().equals(""))
            throw new EmptyCategoryException("Thông tin danh mục không được để trống");

        ProductCategory productCategory = ProductCategory.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .description(categoryDto.getDescription())
                .build();
        repository.save(productCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        ProductCategory category = repository.findById(id).orElseThrow(() -> new CategoryNotFoundException("không tồn tại danh mục này"));
        repository.delete(category);
    }

    @Override
    public List<ProductCategoryDto> getAllCategories() {
        return repository.findAll().stream()
                .map(category -> ProductCategoryDto.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .description(category.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public ProductCategoryResponse getAllCategories(Pageable pageable) {

        Page<ProductCategory> page = repository.findAll(pageable);

        List<ProductCategoryDto> categories = page.get()
                .map(category -> ProductCategoryDto.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .description(category.getDescription())
                        .build())
                .collect(Collectors.toList());

        ProductCategoryResponse categoryResponse = ProductCategoryResponse.builder()
                .data(categories)
                .isFirstPage(page.isFirst())
                .isLastPage(page.isLast())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElement(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .build();

        return categoryResponse;
    }

    @Override
    public ProductCategoryResponse getAllCategoriesByName(String name, Pageable pageable) {
        Page<ProductCategory> page = repository.findAllByName(name, pageable);

        List<ProductCategoryDto> categories = page.get()
                .map(category -> ProductCategoryDto.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .description(category.getDescription())
                        .build())
                .collect(Collectors.toList());

        ProductCategoryResponse categoryResponse = ProductCategoryResponse.builder()
                .data(categories)
                .isFirstPage(page.isFirst())
                .isLastPage(page.isLast())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElement(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .build();

        return categoryResponse;
    }
}
