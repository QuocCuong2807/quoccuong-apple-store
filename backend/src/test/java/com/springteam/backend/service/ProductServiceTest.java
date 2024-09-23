package com.springteam.backend.service;

import com.cloudinary.Api;
import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
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
import com.springteam.backend.service.impl.ProductServiceImpl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {
    @Mock
    private IProductRepository productRepository;
    @Mock
    private IProductImagesRepository productImagesRepository;
    @Mock
    private ICategoryRepository categoryRepository;
    @Mock
    private Cloudinary cloudinary;
    @Mock
    private EntityManager entityManager;
    @Mock
    private MultipartFile imageFile1;
    @Mock
    private Api api;
    @Mock
    private MultipartFile imageFile2;
    @InjectMocks
    private ProductServiceImpl productService;
    private final String PRICE_DESCENDING_FILTER = "DESC";
    private final String PRICE_ASCENDING_FILTER = "ASC";
    private final String ALL_SERIES = "ALL";


    @Test
    public void testGetAllProducts_FilterByCategoryAndPrice_ShouldReturnProductResponseAscendingByPrice() {
        // Giả lập dữ liệu đầu vào
        String filteredCategory = "1";  // Một danh mục hợp lệ
        int p = 0;  // Trang đầu tiên
        Pageable pageable = PageRequest.of(p, 8, Sort.by("price").ascending());  // Sort by price descending
        ProductCategory category = ProductCategory.builder().id(1L).name("iphone").description("dòng mac").build();

        // Giả lập dữ liệu trả về từ repository
        Product product1 = Product.builder().id(1L).category(category).name("iphone 13").price(200L).build();
        Product product2 = Product.builder().id(2L).name("iphone 14").category(category).price(300L).build();
        Product product3 = Product.builder().id(3L).name("iphone 15").category(category).price(400L).build();
        Product product4 = Product.builder().id(4L).name("iphone 16").category(category).price(500L).build();


        List<Product> productList = Arrays.asList(product1, product2, product3, product4);
        Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());  // Giả lập trang có 6 sản phẩm

        // Giả lập hành vi của repository
        when(productRepository.findProductsByCategoryIdOrderByPrice(eq(1L), any(Pageable.class))).thenReturn(productPage);

        // Gọi phương thức service để kiểm tra kết quả
        ProductResponse response = productService.getAllProducts(filteredCategory, PRICE_ASCENDING_FILTER, p);

        // Kiểm tra các giá trị trong response
        assertNotNull(response);
        assertEquals(productList.size(), response.getData().size());  // Có 2 sản phẩm trả về

        //assert price is ascending
        boolean isAsc = IntStream.range(0, productList.size() - 1).allMatch(i -> productList.get(i).getPrice() <= productList.get(i + 1).getPrice());
        assertTrue(isAsc);

        //assert name contain iphone
        response.getData().forEach(product -> assertTrue(product.getName().contains("iphone")));

        //Kiểm tra các thông tin phân trang
        assertTrue(response.isFirstPage());
        assertTrue(response.isLastPage());
        assertEquals(0, response.getPageNumber());
        assertEquals(8, response.getPageSize());
        assertEquals(productList.size(), response.getTotalElement());
        assertEquals(1, response.getTotalPage());

        // Kiểm tra repository interaction
        Mockito.verify(productRepository).findProductsByCategoryIdOrderByPrice(Long.valueOf(filteredCategory), pageable);
    }

    @Test
    public void testGetAllProducts_FilterByCategoryAndPrice_ShouldReturnProductResponseDescendingByPrice() {
        // Giả lập dữ liệu đầu vào
        String filteredCategory = "1";  // Một danh mục hợp lệ
        int p = 0;  // Trang đầu tiên
        Pageable pageable = PageRequest.of(p, 8, Sort.by("price").descending());  // Sort by price descending
        ProductCategory category = ProductCategory.builder().id(1L).name("iphone").description("dòng mac").build();

        // Giả lập dữ liệu trả về từ repository
        Product product1 = Product.builder().id(1L).category(category).name("iphone 13").price(500L).build();
        Product product2 = Product.builder().id(2L).name("iphone 14").category(category).price(400L).build();
        Product product3 = Product.builder().id(3L).name("iphone 15").category(category).price(300L).build();
        Product product4 = Product.builder().id(4L).name("iphone 16").category(category).price(200L).build();


        List<Product> productList = Arrays.asList(product1, product2, product3, product4);
        Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());

        // Giả lập hành vi của repository
        when(productRepository.findProductsByCategoryIdOrderByPrice(eq(1L), any(Pageable.class))).thenReturn(productPage);

        // Gọi phương thức service để kiểm tra kết quả
        ProductResponse response = productService.getAllProducts(filteredCategory, PRICE_DESCENDING_FILTER, p);

        // Kiểm tra các giá trị trong response
        assertNotNull(response);
        assertEquals(productList.size(), response.getData().size());

        //assert price is ascending
        boolean isAsc = IntStream.range(0, productList.size() - 1).allMatch(i -> productList.get(i).getPrice() >= productList.get(i + 1).getPrice());
        assertTrue(isAsc);

        //assert name contain iphone
        response.getData().forEach(product -> assertTrue(product.getName().contains("iphone")));

        //Kiểm tra các thông tin phân trang
        assertTrue(response.isFirstPage());
        assertTrue(response.isLastPage());
        assertEquals(0, response.getPageNumber());
        assertEquals(8, response.getPageSize());
        assertEquals(productList.size(), response.getTotalElement());
        assertEquals(1, response.getTotalPage());

        // Kiểm tra repository interaction
        Mockito.verify(productRepository).findProductsByCategoryIdOrderByPrice(Long.valueOf(filteredCategory), pageable);
    }

    @Test
    void testGetAllProduct_FillDataByPrice_ShouldReturnProductResponseAscendingByPrice() {

        /************************SETUP-DATA-STEP******************************/
        //mock page, pageable, category
        int p = 0;
        Pageable pageable = PageRequest.of(0, 5, Sort.by("price").ascending());
        ProductCategory iphone = ProductCategory.builder().id(1L).name("iphone").description("dòng mac").build();
        ProductCategory mac = ProductCategory.builder().id(2L).name("mac").description("dòng mac").build();

        //mock product list return from repository
        Product product1 = Product.builder().id(1L).category(iphone).name("iphone 13").price(200L).build();
        Product product2 = Product.builder().id(2L).name("iphone 14").category(iphone).price(300L).build();
        Product product3 = Product.builder().id(3L).name("mac m2").category(mac).price(400L).build();
        Product product4 = Product.builder().id(4L).name("mac m3").category(mac).price(500L).build();
        List<Product> productList = Arrays.asList(product1, product2, product3, product4);

        //mock page
        Page page = new PageImpl(productList, pageable, productList.size());

        /************************EXECUTE-STEP******************************/
        //set behavior for mock object method
        when(productRepository.findAll(pageable)).thenReturn(page);
        //execute test method
        ProductResponse response = productService.getAllProducts(ALL_SERIES, PRICE_ASCENDING_FILTER, p);

        /************************ASSERT-STEP******************************/
        //assert product prices are ascending
        boolean isAsc = IntStream.range(0, response.getData().size() - 1)
                .allMatch(i -> response.getData().get(i).getPrice() <= response.getData().get(i).getPrice());
        assertTrue(isAsc);

        //assert current page is first page or last page
        assertTrue(response.isFirstPage());
        assertTrue(response.isLastPage());

        //assert size of data
        assertEquals(4, response.getData().size());

        //assert page size
        assertEquals(5, response.getPageSize());

        //assert data size equal product list size
        assertEquals(productList.size(), response.getData().size());

        //assert total page
        assertEquals(1, response.getTotalPage());

        //assert mock method is called at least 1 time.
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetAllProduct_FillDataByPrice_ShouldReturnProductResponseDescendingByPrice() {

        /************************SETUP-DATA-STEP******************************/
        //mock page, pageable, category
        int p = 0;
        Pageable pageable = PageRequest.of(0, 5, Sort.by("price").ascending());
        ProductCategory iphone = ProductCategory.builder().id(1L).name("iphone").description("dòng mac").build();
        ProductCategory mac = ProductCategory.builder().id(2L).name("mac").description("dòng mac").build();

        //mock product list return from repository
        Product product1 = Product.builder().id(1L).category(iphone).name("iphone 13").price(500L).build();
        Product product2 = Product.builder().id(2L).name("iphone 14").category(iphone).price(400L).build();
        Product product3 = Product.builder().id(3L).name("mac m2").category(mac).price(300L).build();
        Product product4 = Product.builder().id(4L).name("mac m3").category(mac).price(200L).build();
        List<Product> productList = Arrays.asList(product1, product2, product3, product4);

        //mock page
        Page page = new PageImpl(productList, pageable, productList.size());

        /************************EXECUTE-STEP******************************/
        //set behavior for mock object method
        when(productRepository.findAll(pageable)).thenReturn(page);
        //execute test method
        ProductResponse response = productService.getAllProducts(ALL_SERIES, PRICE_ASCENDING_FILTER, p);

        /************************ASSERT-STEP******************************/
        //assert product prices are ascending
        boolean isAsc = IntStream.range(0, response.getData().size() - 1)
                .allMatch(i -> response.getData().get(i).getPrice() >= response.getData().get(i).getPrice());
        assertTrue(isAsc);

        //assert current page is first page or last page
        assertTrue(response.isFirstPage());
        assertTrue(response.isLastPage());

        //assert size of data
        assertEquals(4, response.getData().size());

        //assert page size
        assertEquals(5, response.getPageSize());

        //assert data size equal product list size
        assertEquals(productList.size(), response.getData().size());

        //assert total page
        assertEquals(1, response.getTotalPage());

        //assert mock method is called at least 1 time.
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetProductOverview_ShouldReturnProductResponseList() {

        /************************SETUP-DATA-STEP******************************/

        Product iphone12 = Product.builder().id(1L).name("iphone 12").price(500L).build();
        Product iphone13 = Product.builder().id(1L).name("iphone 13").price(500L).build();
        Product iphone14 = Product.builder().id(2L).name("iphone 14").price(400L).build();
        Product iphone15 = Product.builder().id(2L).name("iphone 15").price(400L).build();

        Product macM1Pro = Product.builder().id(3L).name("mac m1 pro").price(300L).build();
        Product macM2Air = Product.builder().id(4L).name("mac m2 air").price(300L).build();
        Product macM3Air = Product.builder().id(4L).name("mac m3 air").price(300L).build();
        Product macM3Max = Product.builder().id(4L).name("mac m3 max").price(800L).build();

        Product ipadAirM1 = Product.builder().id(4L).name("ipad air m1").price(500L).build();
        Product ipadAirM2 = Product.builder().id(4L).name("ipad air m2").price(300L).build();
        Product ipadProM3 = Product.builder().id(4L).name("ipad pro m3").price(700L).build();
        Product ipadProM2 = Product.builder().id(4L).name("ipad pro m2").price(600L).build();

        List<Product> iphoneList = Arrays.asList(iphone12, iphone13, iphone14, iphone15);
        List<Product> macList = Arrays.asList(macM1Pro, macM3Air, macM3Max, macM2Air);
        List<Product> ipadList = Arrays.asList(ipadAirM1, ipadAirM2, ipadProM3, ipadProM2);

        ProductCategory iphone = ProductCategory.builder().id(1L).name("iPhone").products(iphoneList).description("dong iphone").build();
        ProductCategory ipad = ProductCategory.builder().id(2L).name("iPad").products(macList).description("dong ipad").build();
        ProductCategory mac = ProductCategory.builder().id(3L).name("Mac").products(ipadList).description("dong mac").build();
        List<ProductCategory> categories = Arrays.asList(iphone, ipad, mac);

        iphone12.setCategory(iphone);
        iphone13.setCategory(iphone);
        iphone14.setCategory(iphone);
        iphone15.setCategory(iphone);

        macM1Pro.setCategory(mac);
        macM2Air.setCategory(mac);
        macM3Air.setCategory(mac);
        macM3Max.setCategory(mac);

        ipadAirM1.setCategory(ipad);
        ipadAirM2.setCategory(ipad);
        ipadProM3.setCategory(ipad);
        ipadProM2.setCategory(ipad);

        /************************EXECUTE-STEP******************************/

        when(categoryRepository.findAll()).thenReturn(categories);
        when(productRepository.findFirst4ByCategoryId(1L)).thenReturn(iphoneList);
        when(productRepository.findFirst4ByCategoryId(2L)).thenReturn(ipadList);
        when(productRepository.findFirst4ByCategoryId(3L)).thenReturn(macList);
        List<ProductOverviewResponse> productOverviewResponse = productService.getProductOverview();

        /************************ASSERT-STEP******************************/
        //assert product list size less or equal 4
        productOverviewResponse.stream().forEach(overview -> assertTrue(overview.getProductDtoList().size() <= 4));
        //assert not null
        assertNotNull(productOverviewResponse);

        verify(categoryRepository).findAll();
        verify(productRepository, times(3)).findFirst4ByCategoryId(any(Long.class));
    }

    @Test
    void testGetProductsByName_ThenReturnProductDtoList() {
        /************************SETUP-DATA-STEP******************************/
        //search param
        String name = "ip";

        //data list by search param
        Product iphone15 = Product.builder().id(2L).name("iphone 15").price(400L).build();
        Product iphone14 = Product.builder().id(2L).name("iphone 14").price(400L).build();
        Product ipadProM2 = Product.builder().id(4L).name("ipad pro m2").price(600L).build();
        List<Product> productByNameList = Arrays.asList(iphone14, iphone15, ipadProM2);

        /************************EXECUTE-STEP******************************/
        when(productRepository.findProductsByName(name)).thenReturn(productByNameList);
        List<ProductDto> productDtoList = productService.getProductsByName(name);

        /************************ASSERT-STEP******************************/
        assertNotNull(productDtoList);
        productDtoList.forEach(productDto -> assertTrue(productDto.getName().contains(name)));
        verify(productRepository).findProductsByName(name);
    }

    @Test
    void testGetValidProductDetail_ShouldReturnProductDto() {
        /************************SETUP-DATA-STEP******************************/
        long id = 3L;
        ProductImages img1 = ProductImages.builder().id(255L).image("https://res.cloudinary.com/ddi6agibv/image/upload/v1720536084/apple-store/ermbvfzxzyzcxcpqxiph.jpg").build();
        ProductImages img2 = ProductImages.builder().id(256L).image("https://res.cloudinary.com/ddi6agibv/image/upload/v1720536086/apple-store/uwkxh8su9qpgkbczibfd.jpg").build();
        ProductImages img3 = ProductImages.builder().id(257L).image("https://res.cloudinary.com/ddi6agibv/image/upload/v1720536088/apple-store/tyz0dihgupr9k6s3rpuy.jpg").build();
        Product iphone13 = Product.builder()
                .id(3L)
                .name("iphone 15")
                .color("Titan")
                .technicalDetails("128GB")
                .price(23000000L)
                .mainImage("https://res.cloudinary.com/ddi6agibv/image/upload/v1720084257/apple-store/ocz3f1k8qhfii7qe5oqb.png")
                .build();

        img1.setProduct(iphone13);
        img2.setProduct(iphone13);
        img3.setProduct(iphone13);

        List<ProductImages> productImages = Arrays.asList(img1, img2, img3);
        iphone13.setImagesList(productImages);

        /************************EXECUTE-STEP******************************/
        when(productRepository.findById(id)).thenReturn(Optional.of(iphone13));
        when(productImagesRepository.findAll()).thenReturn(productImages);
        ProductDto productDetail = productService.getProductDetail(id);

        /************************ASSERT-STEP******************************/
        assertNotNull(productDetail);
        assertEquals(iphone13.getId(), productDetail.getId());
        assertEquals(iphone13.getMainImage(), productDetail.getImage());
        List<String> imgList = iphone13.getImagesList().stream().map(img -> img.getImage()).collect(Collectors.toList());
        assertIterableEquals(imgList, productDetail.getImageList());
        verify(productRepository).findById(id);
    }

    @Test
    void testInsertNewProduct_Success() throws IOException {
        // Giả lập kết quả trả về và behavior từ Cloudinary
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "http://res.cloudinary.com/demo/image/upload/mainImage.jpg");
        Uploader uploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(uploadResult);

        //Giả lập danh mục sản phẩm từ repository
        Long categoryId = 1L;
        ProductCategory category = ProductCategory.builder().id(categoryId).build();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Giả lập sản phẩm mới
        Product product = Product.builder().name("iphone 13").category(category).build();

        // Giả lập danh sách ảnh (imageList)
        when(imageFile1.getBytes()).thenReturn("file1data".getBytes());
        when(imageFile2.getBytes()).thenReturn("file2data".getBytes());
        List<MultipartFile> imageList = Arrays.asList(imageFile1, imageFile2);

        // Gọi phương thức insertNewProduct
        productService.insertNewProduct(product, categoryId, imageFile1, imageList);

        // Kiểm tra xem các giá trị có được thiết lập đúng hay không
        assertEquals(category, product.getCategory());
        assertEquals("http://res.cloudinary.com/demo/image/upload/mainImage.jpg", product.getMainImage());
        assertEquals(imageList.size(), product.getImagesList().size());
        verify(productRepository, times(1)).save(product);
        verify(cloudinary.uploader(), times(3)).upload(any(byte[].class), anyMap());
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void testInsertNewProduct_FailToLoadCategory() throws IOException {
        Map<String, String> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "http://res.cloudinary.com/demo/image/upload/mainImage.jpg");
        Uploader uploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(uploader);
        when(cloudinary.uploader().upload(any(byte[].class), anyMap())).thenReturn(uploadResult);


        Long categoryId = 0L;
        Product product = Product.builder().id(1L).name("iphone").build();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        when(imageFile1.getBytes()).thenReturn("file1data".getBytes());
        when(imageFile2.getBytes()).thenReturn("file2data".getBytes());
        List<MultipartFile> imageList = Arrays.asList(imageFile1, imageFile2);

        assertThrows(CategoryNotFoundException.class, () -> productService.insertNewProduct(product, categoryId, imageFile1, imageList));
    }

    @Test
    void testInsertNewProduct_FailToUploadImage() throws IOException {
        when(imageFile1.getBytes()).thenReturn("file1data".getBytes());
        Uploader uploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(uploader);
        when(cloudinary.uploader().upload(any(byte[].class), anyMap())).thenThrow(new ImageNotFoundException("file not found"));

        Long categoryId = 1L;
        ProductCategory category = ProductCategory.builder().id(categoryId).name("iphone").build();
        Product product = Product.builder().name("iphone 12").build();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        List<MultipartFile> imageList = Arrays.asList(imageFile1, imageFile2);
        assertThrows(ImageNotFoundException.class, () -> productService.insertNewProduct(product, categoryId, imageFile1, imageList));
    }

    @Test
    void testUpdateProduct_Success() throws IOException {
        //set-up cloudinary
        when(cloudinary.api()).thenReturn(api);
        when(imageFile1.getBytes()).thenReturn("file1data".getBytes());
        when(imageFile2.getBytes()).thenReturn("file2data".getBytes());
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "http://res.cloudinary.com/demo/image/upload/mainImage.jpg");
        Uploader uploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(uploadResult);

        //set-up image
        List<MultipartFile> imageList = Arrays.asList(imageFile1, imageFile2);
        Optional<List<MultipartFile>> optImgList = Optional.of(imageList);

        //set-up category
        Long categoryId = 1L;
        ProductCategory category = ProductCategory.builder().id(categoryId).name("iphone").build();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        //set-up product
        Long productId = 1L;
        ProductImages img1 = ProductImages.builder().image("https://res.cloudinary.com/demo/image/upload/mainImage.jpg").build();
        ProductImages img2 = ProductImages.builder().image("https://res.cloudinary.com/demo/image/upload/mainImage2.jpg").build();
        List<ProductImages> productImages = Arrays.asList(img1, img2);
        Product product = Product.builder().id(productId).category(category).name("iphone 12").price(200L).mainImage("https://res.cloudinary.com/demo/image/upload/mainImage.jpg").imagesList(productImages).build();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        //set-up product dto and update product
        ProductDto productDto = ProductDto.builder().id(1L).name("iphone 13").price(300L).build();
        Product newProduct = Product.builder().id(productId).name(productDto.getName()).build();
        when(productRepository.save(product)).thenReturn(newProduct);

        //execute
        productService.updateProduct(productDto,categoryId, Optional.of(imageFile1), optImgList);

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_FailToLoadCategory(){
        Long categoryId = 0L;
        Long productId = 1L;
        Product product = Product.builder().id(productId).build();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(productId)).thenThrow(CategoryNotFoundException.class);

        ProductDto productDto = ProductDto.builder().id(productId).build();

        assertThrows(CategoryNotFoundException.class, () -> productService.updateProduct(productDto, categoryId,Optional.of(imageFile1), Optional.of(new ArrayList<>())));

    }

    @Test
    void testUpdateProduct_FailToLoadProduct() {
        Long categoryId = 1L;
        Long productId = 0L;
        ProductCategory category = ProductCategory.builder().id(categoryId).build();
        when(categoryRepository.findById(productId)).thenReturn(Optional.of(category));
        when(productRepository.findById(productId)).thenThrow(ProductNotFoundException.class);

        ProductDto productDto = ProductDto.builder().id(productId).build();

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(productDto, categoryId,Optional.of(imageFile1), Optional.of(new ArrayList<>())));
    }
}
