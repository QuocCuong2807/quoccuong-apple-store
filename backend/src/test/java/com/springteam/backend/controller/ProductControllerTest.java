package com.springteam.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springteam.backend.dto.ProductCategoryDto;
import com.springteam.backend.dto.ProductDto;
import com.springteam.backend.dto.ProductOverviewResponse;
import com.springteam.backend.dto.ProductResponse;
import com.springteam.backend.entity.Product;
import com.springteam.backend.exception.ProductNotFoundException;
import com.springteam.backend.service.ICategoryService;
import com.springteam.backend.service.IProductService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IProductService productService;
    private ProductCategoryDto iphone;
    private ProductCategoryDto ipad;
    private ProductResponse productResponse;
    private List<ProductDto> productDtos;
    private List<ProductDto> ipadList;
    private Pageable pageable;

    @BeforeEach
    void beforeEach() {
        pageable = PageRequest.of(0, 1);

        iphone = ProductCategoryDto.builder()
                .id(1L)
                .name("iphone")
                .description("dong iphone")
                .build();

        ProductDto product1 = ProductDto.builder()
                .id(1L)
                .name("IPHONE 12")
                .price(200L)
                .color("TITAN")
                .description("sp1")
                .technicalDetails("256GB")
                .category(iphone)
                .build();
        ProductDto product2 = ProductDto.builder()
                .id(2L)
                .name("IPHONE 13")
                .price(300L)
                .color("TITAN")
                .description("sp2")
                .technicalDetails("256GB")
                .category(iphone)
                .build();
        ProductDto product3 = ProductDto.builder()
                .id(3L)
                .name("IPHONE 14")
                .price(300L)
                .color("TITAN")
                .description("sp3")
                .technicalDetails("256GB")
                .category(iphone)
                .build();

        productDtos = Arrays.asList(product1, product2, product3);

        productResponse = ProductResponse.builder()
                .data(productDtos)
                .pageNumber(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalElement(productDtos.size())
                .totalPage(3)
                .isFirstPage(true)
                .isLastPage(false)
                .build();
        ipad = ProductCategoryDto.builder()
                .id(2L)
                .name("ipad")
                .description("dong iphone")
                .build();
        ProductDto product4 = ProductDto.builder()
                .id(4L)
                .name("IPAD AIR M1")
                .price(300L)
                .color("TITAN")
                .description("sp3")
                .technicalDetails("256GB")
                .category(ipad)
                .build();
        ProductDto product5 = ProductDto.builder()
                .id(5L)
                .name("IPAD AIR M2")
                .price(300L)
                .color("TITAN")
                .description("sp3")
                .technicalDetails("256GB")
                .category(ipad)
                .build();
        ProductDto product6 = ProductDto.builder()
                .id(6L)
                .name("IPAD AIR M3")
                .price(300L)
                .color("TITAN")
                .description("sp3")
                .technicalDetails("256GB")
                .category(ipad)
                .build();
        ipadList = Arrays.asList(product4, product5, product6);

    }

    @Test
    void testGetProduct() throws Exception {

        String categoryFilter = "1";
        String priceFilter = "ASC";

        when(productService.getAllProducts(categoryFilter, priceFilter, 0)).thenReturn(productResponse);

        mockMvc.perform(get("/api/v1/products")
                        .param("category", categoryFilter)
                        .param("price", priceFilter)
                        .param("p", String.valueOf(0))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.size()").value(3))
                .andExpect(jsonPath("$.firstPage").value(true))
                .andExpect(jsonPath("$.lastPage").value(false))
                .andExpect(jsonPath("$.totalPage").value(3))
                .andExpect(jsonPath("$.pageSize").value(1))
                .andReturn();

        verify(productService).getAllProducts(categoryFilter, priceFilter, 0);
    }

    @Test
    void testGetProductByCategoryAndSeries() throws Exception {
        String categoryFilter = "2";
        String seriesFilter = "IPAD AIR";
        String priceFilter = "ASC";

        productResponse = ProductResponse.builder()
                .data(ipadList)
                .pageNumber(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalElement(ipadList.size())
                .totalPage(3)
                .isFirstPage(true)
                .isLastPage(false)
                .build();

        when(productService.getProductsBySeries(seriesFilter, priceFilter, categoryFilter, 0)).thenReturn(productResponse);

        mockMvc.perform(get("/api/v2/products")
                        .param("series", seriesFilter)
                        .param("price", priceFilter)
                        .param("category", categoryFilter)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name", containsString("IPAD AIR")))
                .andReturn();
    }

    @Test
    void testGetProductByName_NoEmptyName() throws Exception {
        String name = "IPHONE";

        when(productService.getProductsByName(name)).thenReturn(productDtos);

        mockMvc.perform(get("/api/v3/products")
                        .param("name", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andReturn();
    }

    @Test
    void testGetProductByName_EmptyName() throws Exception {
        String name = "";
        productDtos = new ArrayList<>();
        when(productService.getProductsByName(name)).thenReturn(productDtos);

        mockMvc.perform(get("/api/v3/products")
                        .param("name", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)))
                .andReturn();
    }

    @Test
    void testGetProductOverview() throws Exception {
        ProductOverviewResponse productOverview1 = ProductOverviewResponse.builder()
                .categoryId(iphone.getId())
                .categoryName(iphone.getName())
                .productDtoList(productDtos)
                .build();
        ProductOverviewResponse productOverview2 = ProductOverviewResponse.builder()
                .categoryId(ipad.getId())
                .categoryName(ipad.getName())
                .productDtoList(ipadList)
                .build();

        List<ProductOverviewResponse> productOverviewResponseList = Arrays.asList(productOverview1, productOverview2);


        when(productService.getProductOverview()).thenReturn(productOverviewResponseList);

        mockMvc.perform(get("/api/product-overviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].productDtoList").isArray())
                .andReturn();

        verify(productService).getProductOverview();
    }

    @Test
    void testGetProductDetails() throws Exception {
        Long productId = 1L;
        ProductDto productDto = ProductDto.builder()
                .id(1L)
                .name("IPHONE 12")
                .price(200L)
                .color("TITAN")
                .description("sp1")
                .technicalDetails("256GB")
                .category(iphone)
                .build();

        when(productService.getProductDetail(productId)).thenReturn(productDto);

        mockMvc.perform(get("/api/product/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andReturn();

        verify(productService).getProductDetail(productId);
    }

    @Test
    @WithMockUser(username = "ad_quoccuong", authorities = {"ADMIN"})
    void testInsertNewProduct_Success() throws Exception {

        // Mock file upload for image and image list
        MockMultipartFile imageFile = new MockMultipartFile("image", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "image-content".getBytes());
        MockMultipartFile imageFile1 = new MockMultipartFile("imageList", "image1.jpg", MediaType.IMAGE_JPEG_VALUE, "image1-content".getBytes());
        MockMultipartFile imageFile2 = new MockMultipartFile("imageList", "image2.jpg", MediaType.IMAGE_JPEG_VALUE, "image2-content".getBytes());

        String categoryId = "1";
        String productName = "iphone 12";
        String price = "1200";
        String technicalDetails = "128GB";
        String color = "TITAN";
        String description = "New iPhone 12";

        Product product = Product.builder()
                .name(productName)
                .price(Long.valueOf(price))
                .technicalDetails(technicalDetails)
                .color(color)
                .description(description)
                .build();

        doNothing().when(productService).insertNewProduct(product, Long.valueOf(categoryId), imageFile, Arrays.asList(imageFile, imageFile1, imageFile2));

        // Perform multipart request
        mockMvc.perform(multipart("/api/product")
                        .file(imageFile)
                        .file(imageFile1)
                        .file(imageFile2)
                        .param("categoryId", categoryId)
                        .param("name", productName)
                        .param("price", price)
                        .param("technicalDetails", technicalDetails)
                        .param("color", color)
                        .param("description", description)
                        .with(request -> {
                            request.setMethod("POST"); // Explicitly set method to POST
                            return request;
                        }))
                .andExpect(status().isCreated())
                .andExpect(content().string("success"));

        // Verify the service was called with the correct parameters
        verify(productService).insertNewProduct(
                any(Product.class),
                Mockito.eq(1L),
                any(MockMultipartFile.class),
                any(List.class)
        );
    }

    @Test
    @WithMockUser(username = "ad_quoccuong", authorities = {"ADMIN"})
    void testInsertNewProduct_MissParams() throws Exception {

        // Mock file upload for image and image list
        MockMultipartFile imageFile = new MockMultipartFile("image", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "image-content".getBytes());
        MockMultipartFile imageFile1 = new MockMultipartFile("imageList", "image1.jpg", MediaType.IMAGE_JPEG_VALUE, "image1-content".getBytes());
        MockMultipartFile imageFile2 = new MockMultipartFile("imageList", "image2.jpg", MediaType.IMAGE_JPEG_VALUE, "image2-content".getBytes());

        String categoryId = "1";
        String productName = "iphone 12";
        String price = "1200";
        String technicalDetails = "128GB";
        String color = "TITAN";
        String description = "New iPhone 12";

        Product product = Product.builder()
                .name(productName)
                .price(Long.valueOf(price))
                .technicalDetails(technicalDetails)
                .color(color)
                .description(description)
                .build();

        doNothing().when(productService).insertNewProduct(product, Long.valueOf(categoryId), imageFile, Arrays.asList(imageFile, imageFile1, imageFile2));

        // Perform multipart request
        mockMvc.perform(multipart("/api/product")
                        .file(imageFile)
                        .file(imageFile1)
                        .param("categoryId", categoryId)
                        .param("name", productName)
                        .param("price", price)
                        .param("color", color)
                        .param("description", description))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value(400));


    }

    @Test
    @WithMockUser(username = "ad_quoccuong", authorities = {"ADMIN"})
    void testUpdateProduct_Success() throws Exception {

        // Mock file upload for image and image list
        MockMultipartFile imageFile = new MockMultipartFile("image", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "image-content".getBytes());
        MockMultipartFile imageFile1 = new MockMultipartFile("imageList", "image1.jpg", MediaType.IMAGE_JPEG_VALUE, "image1-content".getBytes());
        MockMultipartFile imageFile2 = new MockMultipartFile("imageList", "image2.jpg", MediaType.IMAGE_JPEG_VALUE, "image2-content".getBytes());

        String categoryId = "1";
        String productId = "1";
        String productName = "iphone 12";
        String price = "1200";
        String technicalDetails = "128GB";
        String color = "TITAN";
        String description = "New iPhone 12";

        ProductDto product = ProductDto.builder()
                .id(Long.valueOf(productId))
                .name(productName)
                .price(Long.valueOf(price))
                .technicalDetails(technicalDetails)
                .color(color)
                .description(description)
                .build();

        doNothing().when(productService).updateProduct(product, Long.valueOf(categoryId), Optional.of(imageFile), Optional.of(Arrays.asList(imageFile, imageFile1, imageFile2)));

        // Perform multipart request
        mockMvc.perform(multipart("/api/product")
                        .param("id", productId)
                        .param("categoryId", categoryId)
                        .param("name", productName)
                        .param("price", price)
                        .param("technicalDetails", technicalDetails)
                        .param("color", color)
                        .param("description", description)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        // Verify the service was called with the correct parameters
        verify(productService).updateProduct(
                any(ProductDto.class),
                Mockito.eq(1L),
                any(Optional.class),
                any(Optional.class)
        );
    }

    @Test
    @WithMockUser(username = "ad_quoccuong", authorities = {"ADMIN"})
    void testUpdateProduct_MissParams() throws Exception {
        // Mock file upload for image and image list
        MockMultipartFile imageFile = new MockMultipartFile("image", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "image-content".getBytes());
        MockMultipartFile imageFile1 = new MockMultipartFile("imageList", "image1.jpg", MediaType.IMAGE_JPEG_VALUE, "image1-content".getBytes());
        MockMultipartFile imageFile2 = new MockMultipartFile("imageList", "image2.jpg", MediaType.IMAGE_JPEG_VALUE, "image2-content".getBytes());
        String categoryId = "1";
        String productId = "1";
        String productName = "iphone 12";
        String price = "1200";
        String technicalDetails = "128GB";
        String color = "TITAN";
        String description = "New iPhone 12";

        ProductDto product = ProductDto.builder()
                .id(Long.valueOf(productId))
                .name(productName)
                .price(Long.valueOf(price))
                .technicalDetails(technicalDetails)
                .color(color)
                .description(description)
                .build();

        doNothing().when(productService).updateProduct(product, Long.valueOf(categoryId), Optional.of(imageFile), Optional.of(Arrays.asList(imageFile, imageFile1, imageFile2)));

        // Perform multipart request
        //miss productId parameter
        mockMvc.perform(multipart("/api/product")
                        .param("categoryId", categoryId)
                        .param("name", productName)
                        .param("price", price)
                        .param("technicalDetails", technicalDetails)
                        .param("color", color)
                        .param("description", description)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @WithMockUser(username = "ad_quoccuong", authorities = {"ADMIN"})
    void testDeleteProduct_Success() throws Exception {
        Long id = 1L;
        doNothing().when(productService).deleteExistedProduct(id);

        mockMvc.perform(delete("/api/product/{id}", id))
                .andExpect(status().isOk())
                .andReturn();
    }
    @Test
    @WithMockUser(username = "ad_quoccuong", authorities = {"ADMIN"})
    void testDeleteProduct_FailToLoadProduct() throws Exception {
        Long id = 0L;
        doThrow(ProductNotFoundException.class).when(productService).deleteExistedProduct(id);

        mockMvc.perform(delete("/api/product/{id}", id))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void testDeleteProduct_FailToAuth() throws Exception {
        Long id = 0L;
        mockMvc.perform(delete("/api/product/{id}", id))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Full authentication is required to access this resource"))
                .andReturn();
    }
}
