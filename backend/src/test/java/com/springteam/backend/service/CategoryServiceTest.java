package com.springteam.backend.service;

import com.springteam.backend.dto.ProductCategoryDto;
import com.springteam.backend.dto.ProductCategoryResponse;
import com.springteam.backend.entity.ProductCategory;
import com.springteam.backend.exception.EmptyCategoryException;
import com.springteam.backend.repository.ICategoryRepository;
import com.springteam.backend.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CategoryServiceTest {
    @Mock
    private ICategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryService;


    @Test
    void insertNewCategoryTest() {


        //null object or empty object name test
        assertThrows(EmptyCategoryException.class, () -> categoryService.insertNewCategory(null));
        assertThrows(EmptyCategoryException.class, () -> categoryService.insertNewCategory(ProductCategoryDto.builder().name(" ").build()));

        ProductCategory newCategory = ProductCategory.builder().name("mac").description("dòng mac").build();
        ProductCategory returnCategory = ProductCategory.builder().id(1L).name("mac").description("dòng mac").build();

        //set behaviour for mock object method
        when(categoryRepository.save(newCategory)).thenReturn(returnCategory);

        //call service
        categoryService.insertNewCategory(ProductCategoryDto.builder().name("mac").description("dòng mac").build());

        //verify mock object method run as least 1 time
        verify(categoryRepository).save(ArgumentMatchers.any(ProductCategory.class));
    }

    @Test
    void updateExistedCategoryTest() {
        //null object or empty object name test
        assertThrows(EmptyCategoryException.class, () -> categoryService.insertNewCategory(null));
        assertThrows(EmptyCategoryException.class, () -> categoryService.insertNewCategory(ProductCategoryDto.builder().name(" ").build()));

        categoryService.updateExistedCategory(ProductCategoryDto.builder().id(1L).name("mac pro").description("dong mac pro").build());

        //verify mock object method run as least 1 time
        verify(categoryRepository).save(ArgumentMatchers.any(ProductCategory.class));
    }

    @Test
    void getAllPagingCategories() {
        //set-up
        Pageable pageable = PageRequest.of(0, 2);
        ProductCategory category = ProductCategory.builder().id(1L).name("MAC").description("dong mac").build();
        ProductCategory category2 = ProductCategory.builder().id(1L).name("IPHONE").description("dong iphone").build();

        List<ProductCategory> productCategories = Arrays.asList(category, category2);
        Page<ProductCategory> page = new PageImpl<>(productCategories, pageable, productCategories.size());

        //set behaviour for mock obj method
        when(categoryRepository.findAll(pageable)).thenReturn(page);

        //execute test method
        ProductCategoryResponse response = categoryService.getAllCategories(pageable);

        //assert
        assertTrue(response.isFirstPage());
        assertTrue(response.isLastPage());
        assertEquals(1,response.getTotalPage());
        assertEquals(1, response.getTotalPage());
        assertEquals(2, response.getTotalElement());
        verify(categoryRepository, times(1)).findAll(pageable);
    }

    @Test
    void getAllCategoriesByName(){

        //set-up
        String name = "iP";
        Pageable pageable = PageRequest.of(0, 5);
        ProductCategory category = ProductCategory.builder().id(1L).name("iPhone").description("dong ipad").build();
        ProductCategory category2 = ProductCategory.builder().id(1L).name("iPad").description("dong ipad").build();
        List<ProductCategory> categories = Arrays.asList(category, category2);
        Page<ProductCategory> page = new PageImpl<>(categories, pageable, categories.size());

        //set behavior for mock obj method
        when(categoryRepository.findAllByName(name, pageable)).thenReturn(page);

        //execute method for testing
        ProductCategoryResponse  response = categoryService.getAllCategoriesByName(name, pageable);

        //assert
        assertTrue(response.isFirstPage());
        assertTrue(response.isLastPage());
        assertEquals(1,response.getTotalPage());
        assertEquals(1, response.getTotalPage());
        assertEquals(2, response.getTotalElement());

        verify(categoryRepository).findAllByName(name, pageable);
    }

}
