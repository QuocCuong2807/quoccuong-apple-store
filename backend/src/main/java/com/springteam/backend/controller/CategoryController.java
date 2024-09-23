package com.springteam.backend.controller;

import com.springteam.backend.dto.ProductCategoryDto;
import com.springteam.backend.dto.ProductCategoryResponse;
import com.springteam.backend.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class CategoryController {
    private ICategoryService categoryService;

    @Autowired
    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/v1/categories")
    public ResponseEntity<ProductCategoryResponse> getAllProductCategoriesWithParam(@RequestParam Optional<String> name, @RequestParam Optional<Integer> p) {
        Pageable pageable = PageRequest.of(p.orElse(0), 3);
        String queryString = name.orElse("");
        ProductCategoryResponse categoryResponse;
        if (queryString.equals(""))
            categoryResponse = categoryService.getAllCategories(pageable);
        else
            categoryResponse = categoryService.getAllCategoriesByName(queryString, pageable);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @GetMapping("/v2/categories")
    public ResponseEntity<List<ProductCategoryDto>> getAllProductCategories() {
        List<ProductCategoryDto> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/category")
    public ResponseEntity<String> addNewProductCategory(@RequestBody ProductCategoryDto categoryDto) {
        categoryService.insertNewCategory(categoryDto); //void
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @PutMapping("/category")
    public ResponseEntity<String> editExistProductCategory(@RequestBody ProductCategoryDto categoryDto) {
        categoryService.updateExistedCategory(categoryDto);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<String> deleteProductCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
