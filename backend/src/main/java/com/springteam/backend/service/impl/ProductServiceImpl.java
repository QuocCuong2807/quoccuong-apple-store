package com.springteam.backend.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.springteam.backend.dto.ProductCategoryDto;
import com.springteam.backend.dto.ProductDto;
import com.springteam.backend.dto.ProductOverviewResponse;
import com.springteam.backend.dto.ProductResponse;
import com.springteam.backend.entity.Product;
import com.springteam.backend.entity.ProductCategory;
import com.springteam.backend.entity.ProductImages;
import com.springteam.backend.exception.CategoryNotFoundException;
import com.springteam.backend.exception.ImageNotFoundException;
import com.springteam.backend.exception.ProductNotFoundException;
import com.springteam.backend.repository.ICategoryRepository;
import com.springteam.backend.repository.IProductImagesRepository;
import com.springteam.backend.repository.IProductRepository;
import com.springteam.backend.service.IProductService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {
    private IProductRepository productRepository;
    private IProductImagesRepository productImagesRepository;
    private ICategoryRepository categoryRepository;
    private EntityManager entityManager;
    private Cloudinary cloudinary;
    private final String PRICE_DESCENDING_FILTER = "DESC";
    private final String PRICE_ASCENDING_FILTER = "ASC";
    private final String ALL_SERIES = "ALL";


    @Autowired
    public ProductServiceImpl(IProductRepository productRepository, IProductImagesRepository productImagesRepository,
                              ICategoryRepository categoryRepository, EntityManager entityManager, Cloudinary cloudinary) {
        this.productRepository = productRepository;
        this.productImagesRepository = productImagesRepository;
        this.categoryRepository = categoryRepository;
        this.entityManager = entityManager;
        this.cloudinary = cloudinary;
    }

    @Override
    public ProductResponse getAllProducts(String filteredCategory, String filteredPrice, int p)   {
        Page<Product> page;
        if (!isDigit(filteredCategory)) {
            page = fillDataByPriceFilter(filteredPrice, p);
        } else {
            page = fillDataByCategoryAndPriceFilter(filteredCategory, filteredPrice, p);
        }

        //convert Product list to ProductDto List
        List<ProductDto> productDtoList = page.get().toList()
                .stream().map(product -> ProductDto.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .category(ProductCategoryDto.builder()
                                .id(product.getCategory().getId())
                                .name(product.getCategory().getName())
                                .description(product.getCategory().getDescription())
                                .build())
                        .price(product.getPrice())
                        .image(product.getMainImage())
                        .color(product.getColor())
                        .description(product.getDescription())
                        .technicalDetails(product.getTechnicalDetails())
                        .build())
                .collect(Collectors.toList());

        //convert to product response
        ProductResponse productResponse = ProductResponse.builder()
                .data(productDtoList)
                .isFirstPage(page.isFirst())
                .isLastPage(page.isLast())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElement(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .build();

        return productResponse;
    }

    @Override
    public ProductResponse getProductsBySeries(String productSeriesFilter, String priceFilter, String categoryId, int p) {
        Page<Product> page;
        if (productSeriesFilter.equals(ALL_SERIES)) {
            page = fillDataByCategoryAndPriceFilter(categoryId, priceFilter, p);
        } else {
            page = fillDataByPriceAndSeriesFilter(productSeriesFilter, priceFilter, Long.valueOf(categoryId), p);
        }
        //convert Product list to ProductDto List
        List<ProductDto> productDtoList = page.get().toList()
                .stream().map(product -> ProductDto.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .category(ProductCategoryDto.builder()
                                .id(product.getCategory().getId())
                                .name(product.getCategory().getName())
                                .description(product.getCategory().getDescription())
                                .build())
                        .price(product.getPrice())
                        .image(product.getMainImage())
                        .color(product.getColor())
                        .description(product.getDescription())
                        .technicalDetails(product.getTechnicalDetails())
                        .build())
                .collect(Collectors.toList());

        //convert to product response
        ProductResponse productResponse = ProductResponse.builder()
                .data(productDtoList)
                .isFirstPage(page.isFirst())
                .isLastPage(page.isLast())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElement(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .build();
        return productResponse;
    }

    @Override
    public List<ProductOverviewResponse> getProductOverview() {
        List<ProductCategory> categories = categoryRepository.findAll();

        List<ProductOverviewResponse> productOverviewResponses =
                categories.stream()
                        .map(category -> ProductOverviewResponse.builder()
                                .categoryId(category.getId())
                                .categoryName(category.getName())
                                .productDtoList(
                                        productRepository.findFirst4ByCategoryId(category.getId()).stream()
                                                .map(product -> ProductDto.builder()
                                                        .id(product.getId())
                                                        .image(product.getMainImage())
                                                        .price(product.getPrice())
                                                        .name(product.getName())
                                                        .build())
                                                .collect(Collectors.toList())
                                )
                                .build())
                        .collect(Collectors.toList());
        return productOverviewResponses;
    }

    @Override
    public List<ProductDto> getProductsByName(String productName) {
        List<Product> products = productRepository.findProductsByName(productName);

        List<ProductDto> productDtos = products.stream()
                .map(product -> ProductDto.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .image(product.getMainImage())
                        .build())
                .collect(Collectors.toList());

        return productDtos;
    }

    @Override
    public ProductDto getProductDetail(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("không tìm thấy sản phẩm"));
        ProductDto productDto = ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .image(product.getMainImage())
                .color(product.getColor())
                .technicalDetails(product.getTechnicalDetails())
                .price(product.getPrice())
                .description(product.getDescription())
                .imageList(product.getImagesList().stream()
                        .map(img -> img.getImage())
                        .collect(Collectors.toList()))
                .build();
        return productDto;
    }


    @Override
    public void insertNewProduct(Product product, Long categoryId, MultipartFile image, List<MultipartFile> imageList) {

        String mainImage = cloudinaryUploadImage(image).get("secure_url").toString();

        List<ProductImages> imageDescList = imageList.stream().map(img -> ProductImages.builder()
                        .image(cloudinaryUploadImage(img).get("secure_url").toString())
                        .product(product)
                        .build())
                .collect(Collectors.toList());

        ProductCategory category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Không tìm thây danh mục phù hợp"));

        product.setCategory(category);
        product.setMainImage(mainImage);
        product.setImagesList(imageDescList);

        productRepository.save(product);
    }

    @Override
    public void updateProduct(ProductDto newProduct, Long categoryId, Optional<MultipartFile> mainImage, Optional<List<MultipartFile>> imageDescription) {

        MultipartFile imageFile = mainImage.orElse(null);
        List<MultipartFile> images = imageDescription.orElse(null);

        //persistence entity
        Product existedProduct = productRepository.findById(newProduct.getId())
                .orElseThrow(() -> new ProductNotFoundException("Sản phẩm không tồn tại"));
        ProductCategory newCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("không tồn tại danh mục"));
        List<ProductImages> productImagesList = existedProduct.getImagesList();

        //update persistence field
        existedProduct.setCategory(newCategory);
        existedProduct.setName(newProduct.getName());
        existedProduct.setPrice(newProduct.getPrice());
        existedProduct.setColor(newProduct.getColor());
        existedProduct.setTechnicalDetails(newProduct.getTechnicalDetails());
        existedProduct.setDescription(newProduct.getDescription());

        String newMainImage = "";

        //handle update product images
        if (imageFile != null && images != null) {
            //delete all images
            deleteCloudinaryImage(existedProduct.getMainImage());
            productImagesList.forEach(img -> {
                deleteCloudinaryImage(img.getImage());
                entityManager.detach(img);
            });
            productImagesRepository.deleteAllByProductId(existedProduct.getId());
            productImagesList = images.stream()
                    .map(file -> ProductImages.builder()
                            .image(cloudinaryUploadImage(file).get("secure_url").toString())
                            .product(existedProduct)
                            .build())
                    .collect(Collectors.toList());

            //upload new images
            newMainImage = cloudinaryUploadImage(imageFile).get("secure_url").toString();
            existedProduct.setMainImage(newMainImage);
            existedProduct.setImagesList(productImagesList);
        }
        else if (imageFile == null && images != null) {
            //delete all image description
            productImagesList.forEach(img -> {
                deleteCloudinaryImage(img.getImage());
                entityManager.detach(img);
            });
            productImagesRepository.deleteAllByProductId(existedProduct.getId());
            //upload new images description
            productImagesList = images.stream()
                    .map(file -> ProductImages.builder()
                            .image(cloudinaryUploadImage(file).get("secure_url").toString())
                            .product(existedProduct)
                            .build())
                    .collect(Collectors.toList());
            existedProduct.setImagesList(productImagesList);
        }
        else if (imageFile != null && images == null) {
            deleteCloudinaryImage(existedProduct.getMainImage());
            newMainImage = cloudinaryUploadImage(imageFile).get("secure_url").toString();
            existedProduct.setMainImage(newMainImage);
        }

        productRepository.save(existedProduct);
    }

    @Override
    public void deleteExistedProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("không tồn tại sản phẩm"));

        //get product images url
        String mainImageUrl = product.getMainImage();
        List<String> imageDescriptionUrl = product.getImagesList()
                .stream()
                .map(productImages -> productImages.getImage())
                .collect(Collectors.toList());

        deleteCloudinaryImage(mainImageUrl);
        product.getImagesList().forEach(img -> img.getImage());
        productRepository.delete(product);
    }


    private boolean isDigit(String s) {

        for (char c : s.toCharArray()
        ) {
            if (!Character.isDigit(c)) return false;
        }

        return true;
    }

    private Page<Product> fillDataByPriceFilter(String filter, int p) {
        Pageable pageable;
        switch (filter) {
            case PRICE_DESCENDING_FILTER:
                pageable = PageRequest.of(p, 5, Sort.by("price").descending());
                return productRepository.findAll(pageable);
            case PRICE_ASCENDING_FILTER:
                pageable = PageRequest.of(p, 5, Sort.by("price").ascending());
                return productRepository.findAll(pageable);
        }
        return null;
    }

    private Page<Product> fillDataByPriceAndSeriesFilter(String seriesFilter, String priceFilter, Long categoryId, int p) {
        Pageable pageable;
        switch (priceFilter) {
            case PRICE_DESCENDING_FILTER:
                pageable = PageRequest.of(p, 8, Sort.by("price").descending());
                return productRepository.findProductBySeriesAndCategory(seriesFilter, categoryId, pageable);
            case PRICE_ASCENDING_FILTER:
                pageable = PageRequest.of(p, 8, Sort.by("price").ascending());
                return productRepository.findProductBySeriesAndCategory(seriesFilter, categoryId, pageable);
        }
        return null;
    }

    private Page<Product> fillDataByCategoryAndPriceFilter(String filteredCategory, String filteredPrice, int p) {
        Pageable pageable;
        switch (filteredPrice) {
            case PRICE_DESCENDING_FILTER:
                pageable = PageRequest.of(p, 8, Sort.by("price").descending());
                return productRepository.findProductsByCategoryIdOrderByPrice(Long.valueOf(filteredCategory), pageable);
            case PRICE_ASCENDING_FILTER:
                pageable = PageRequest.of(p, 8, Sort.by("price").ascending());
                return productRepository.findProductsByCategoryIdOrderByPrice(Long.valueOf(filteredCategory), pageable);
        }

        return null;
    }

    private void deleteCloudinaryImage(String mainImageUrl) {
        String mainPublicId = getPublicIdFromImageUrl(mainImageUrl);

        //delete cloudinary main image
        try {
            cloudinary.api().deleteResources(Arrays.asList(mainPublicId), ObjectUtils.asMap("type", "upload", "resource_type", "image"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map cloudinaryUploadImage(MultipartFile file) {

        Map data;
        Map params = ObjectUtils.asMap(
                "folder", "docker",
                "resource_type", "image"
        );
        try {
            data = this.cloudinary.uploader().upload(file.getBytes(), params);
        } catch (IOException e) {
            throw new ImageNotFoundException("file not found");
        }

        return data;
    }

    public String getPublicIdFromImageUrl(String url) {

        String publicId = "";
        String[] mainSplitItems = url.split("/"); //["https:", ...., "apple-store","publicId.jpg"]
        String folderName = mainSplitItems[mainSplitItems.length - 2];//apple-store
        String fileName = mainSplitItems[mainSplitItems.length - 1].split("\\.")[0];//publicId.jpg
        publicId = folderName + "/" + fileName; //actual publicId: apple-store/publicId.jpg
        return publicId;
    }


}
