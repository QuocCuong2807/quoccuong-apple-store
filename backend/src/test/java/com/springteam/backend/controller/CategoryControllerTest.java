package com.springteam.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springteam.backend.dto.ProductCategoryDto;
import com.springteam.backend.dto.ProductCategoryResponse;
import com.springteam.backend.exception.CategoryNotFoundException;
import com.springteam.backend.service.ICategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ICategoryService categoryService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllCategoriesWithNoParams() throws Exception {

        // Thiết lập phản hồi mock
        Pageable pageable = PageRequest.of(0, 3);
        ProductCategoryResponse response = ProductCategoryResponse.builder()
                .data(Arrays.asList(ProductCategoryDto.builder().id(1L).name("category").description("desc").build()))
                .pageNumber(0)
                .pageSize(3)
                .totalElement(1)
                .totalPage(1)
                .isFirstPage(true)
                .isLastPage(true)
                .build();

        // Thiết lập mock cho service
        when(categoryService.getAllCategories(pageable)).thenReturn(response);

        // Thực hiện yêu cầu và kiểm tra phản hồi
        mockMvc.perform(get("/api/v1/categories") // Thay đổi URL cho phù hợp với endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("category"))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.pageSize").value(3))
                .andExpect(jsonPath("$.totalElement").value(1))
                .andExpect(jsonPath("$.totalPage").value(1))
                .andExpect(jsonPath("$.firstPage").value(true))
                .andExpect(jsonPath("$.lastPage").value(true))
                .andReturn();

        verify(categoryService).getAllCategories(pageable);
    }

    @Test
    void testGetAllCategoriesWithNameParams() throws Exception {
        Optional<String> name = Optional.of("iphone");
        String queryString = name.get();
        Pageable pageable = PageRequest.of(0, 3);

        List<ProductCategoryDto> categoryDtoList = Arrays.asList(ProductCategoryDto.builder().id(1L).name("iphone").description("dong iphone").build());
        ProductCategoryResponse response = ProductCategoryResponse.builder()
                .data(categoryDtoList)
                .pageNumber(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalElement(categoryDtoList.size())
                .totalPage(1)
                .isFirstPage(true)
                .isLastPage(true)
                .build();

        when(categoryService.getAllCategoriesByName(queryString, pageable)).thenReturn(response);

        mockMvc.perform(get("/api/v1/categories?name=iphone").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("iphone"))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.pageSize").value(3))
                .andExpect(jsonPath("$.totalElement").value(1))
                .andExpect(jsonPath("$.totalPage").value(1))
                .andExpect(jsonPath("$.firstPage").value(true))
                .andExpect(jsonPath("$.lastPage").value(true))
                .andReturn();

        verify(categoryService).getAllCategoriesByName(queryString,pageable);
    }

    @Test
    void testGetAllCategories() throws Exception {
        ProductCategoryDto iphone = ProductCategoryDto.builder().id(1L).name("iphone").description("dong iphone").build();
        ProductCategoryDto ipad = ProductCategoryDto.builder().id(1L).name("ipad").description("dong ipad").build();
        ProductCategoryDto mac = ProductCategoryDto.builder().id(1L).name("macbook").description("dong mac").build();

        List<ProductCategoryDto> categories = Arrays.asList(iphone,ipad, mac);

        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/v2/categories").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andReturn();

        verify(categoryService).getAllCategories();
    }

    @Test
    @WithMockUser(username = "ad_quoccuong", authorities = {"ADMIN"})
    void testInsertNewCategory() throws Exception {
        ProductCategoryDto categoryDto = ProductCategoryDto.builder().id(1L).name("iphone").description("dong iphone").build();
        mockMvc.perform(post("/api/category").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("success"))
                .andReturn();

        verify(categoryService).insertNewCategory(any(ProductCategoryDto.class));
    }

    @Test
    @WithMockUser(username = "ad_quoccuong", authorities = {"ADMIN"})
    void testUpdateExistedCategory() throws Exception {
        ProductCategoryDto categoryDto = ProductCategoryDto.builder().id(1L).name("iphone").description("dong iphone").build();
        mockMvc.perform(put("/api/category").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("success"))
                .andReturn();

        verify(categoryService).updateExistedCategory(any(ProductCategoryDto.class));
    }

    @Test
    @WithMockUser(username = "ad_quoccuong", authorities = {"ADMIN"})
    void testDeleteCategory_Success() throws Exception {
        Long id = 1L;
        doNothing().when(categoryService).deleteCategory(id);
        mockMvc.perform(delete("/api/category/{id}",id))
                .andExpect(status().isOk())
                .andExpect(content().string("success"))
                .andReturn();

        verify(categoryService).deleteCategory(id);
    }

    @Test
    @WithMockUser(username = "ad_quoccuong", authorities = {"ADMIN"})
    void testDeleteCategory_FailToLoadCategory() throws Exception {
        Long id = 0L;
        doThrow(CategoryNotFoundException.class).when(categoryService).deleteCategory(id);
        mockMvc.perform(delete("/api/category/{id}",id))
                .andExpect(status().is4xxClientError())
                .andExpect(status().is(400))
                .andReturn();

        verify(categoryService).deleteCategory(id);
    }
}
