package com.springteam.backend.service;

import com.springteam.backend.dto.ProductDto;
import com.springteam.backend.dto.ProductOverviewResponse;
import com.springteam.backend.dto.ProductResponse;
import com.springteam.backend.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    ProductResponse getAllProducts(String categoryFilter, String priceFilter, int p);
    ProductResponse getProductsBySeries(String productSeriesFilter, String priceFilter,String categoryId, int p);
    List<ProductOverviewResponse> getProductOverview();
    List<ProductDto> getProductsByName(String productName);
    ProductDto getProductDetail(Long id);
    void insertNewProduct(Product product, Long categoryId, MultipartFile image, List<MultipartFile> imageList);
    void updateProduct(ProductDto newProduct, Long categoryId, Optional<MultipartFile> mainImage, Optional<List<MultipartFile>> imageDescription);
    void deleteExistedProduct(Long id);
}
