package com.springteam.backend.controller;

import com.springteam.backend.annotation.IsNumber;
import com.springteam.backend.dto.ProductDto;
import com.springteam.backend.dto.ProductOverviewResponse;
import com.springteam.backend.dto.ProductResponse;
import com.springteam.backend.entity.Product;
import com.springteam.backend.service.ICategoryService;
import com.springteam.backend.service.IProductService;
import com.springteam.backend.service.impl.CategoryServiceImpl;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class ProductController {

    private IProductService productService;
    @Autowired
    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/v1/products")
    public ResponseEntity<ProductResponse> getProducts(@RequestParam(name = "category") String categoryFilter,
                                                       @RequestParam(name = "price") String priceFilter,
                                                       @RequestParam Optional<Integer> p) {

        ProductResponse productResponse = productService.getAllProducts(categoryFilter, priceFilter, p.orElse(0));
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/v2/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@RequestParam(name = "series") String productSeriesFilter,
                                                                 @RequestParam(name = "price") String priceFilter,
                                                                 @RequestParam(name = "category") String categoryId,
                                                                 @RequestParam Optional<Integer> p) {
        ProductResponse productResponse =
                productService.getProductsBySeries(productSeriesFilter, priceFilter, categoryId, p.orElse(0));
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/v3/products")
    public ResponseEntity<List<ProductDto>> getProductsByName(@RequestParam(name = "name") String productName) {

        List<ProductDto> productDtos = new ArrayList<>();
        productDtos = productName.trim().equals("") || productName == null
                ? productDtos
                : productService.getProductsByName(productName);

        return new ResponseEntity<>(productDtos, HttpStatus.OK);
    }

    @GetMapping("/product-overviews")
    public ResponseEntity<List<ProductOverviewResponse>> getProductOverview() {
        List<ProductOverviewResponse> productOverviewResponses = productService.getProductOverview();
        return new ResponseEntity<>(productOverviewResponses, HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductDto> getProductDetail(@PathVariable String id) {
        ProductDto productDto = productService.getProductDetail(Long.valueOf(id));
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }


    @PostMapping(value = "/product", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> addNewProduct(@RequestParam @NotNull @NotBlank @IsNumber @Min(1) String categoryId,
                                                @RequestParam @NotNull @NotBlank String name,
                                                @RequestParam @NotNull @NotBlank @IsNumber @Min(0) String price,
                                                @RequestParam @NotNull @NotBlank String technicalDetails,
                                                @RequestParam @NotNull @NotBlank String color,
                                                @RequestParam @NotNull @NotBlank String description,
                                                @RequestParam @NotNull MultipartFile image,
                                                @RequestParam @NotNull List<MultipartFile> imageList) {
        Product product = Product.builder()
                .name(name)
                .price(Long.valueOf(price))
                .technicalDetails(technicalDetails)
                .color(color)
                .description(description)
                .build();

        productService.insertNewProduct(product, Long.valueOf(categoryId), image, imageList);
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @PutMapping(value = "/product", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> updateProduct(@RequestParam @NotNull @NotBlank @IsNumber @Min(1) String id,
                                                @RequestParam @NotNull @NotBlank @IsNumber @Min(1) String categoryId,
                                                @RequestParam @NotNull @NotBlank String name,
                                                @RequestParam @NotNull @NotBlank @IsNumber @Min(0) String price,
                                                @RequestParam @NotNull @NotBlank String technicalDetails,
                                                @RequestParam @NotNull @NotBlank String color,
                                                @RequestParam @NotNull @NotBlank String description,
                                                @RequestParam Optional<MultipartFile> image,
                                                @RequestParam Optional<List<MultipartFile>> imageList
    ) {

        ProductDto product = ProductDto.builder()
                .id(Long.valueOf(id))
                .name(name)
                .price(Long.valueOf(price))
                .color(color)
                .technicalDetails(technicalDetails)
                .description(description)
                .build();

        productService.updateProduct(product, Long.valueOf(categoryId), image, imageList);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteExistedProduct(@PathVariable Long id) {
        productService.deleteExistedProduct(id);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
