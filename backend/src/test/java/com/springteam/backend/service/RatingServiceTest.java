package com.springteam.backend.service;

import com.springteam.backend.dto.ProductCategoryDto;
import com.springteam.backend.dto.ProductRatingDto;
import com.springteam.backend.dto.RatingResponse;
import com.springteam.backend.dto.StarRatingOverviewDto;
import com.springteam.backend.entity.Product;
import com.springteam.backend.entity.ProductRating;
import com.springteam.backend.entity.UserEntity;
import com.springteam.backend.exception.ProductNotFoundException;
import com.springteam.backend.exception.UsernameNotFoundException;
import com.springteam.backend.repository.IProductRepository;
import com.springteam.backend.repository.IRatingRepository;
import com.springteam.backend.repository.IUserRepository;
import com.springteam.backend.service.impl.RatingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class RatingServiceTest {
    @Mock
    private IRatingRepository ratingRepository;
    @Mock
    private IUserRepository userRepository;
    @Mock
    private IProductRepository productRepository;
    private Product product;
    private UserEntity user;
    private ProductRatingDto productRatingDto;
    private List<ProductRating> productRatingList;
    @InjectMocks
    private RatingServiceImpl ratingService;

    @BeforeEach
    void beforeEach() {
        product = Product.builder().id(1L).name("iphone").color("TITAN").price(5L).build();
        user = UserEntity.builder().username("us_quoccuong").build();

        productRatingDto = ProductRatingDto.builder()
                .productId(product.getId())
                .date("1672531199000")
                .ratingContent("dep xin chat")
                .userName(user.getUsername())
                .star(5)
                .build();
        long timestamp = Long.valueOf(productRatingDto.getDate());
        Instant instant = Instant.ofEpochMilli(timestamp);
        ProductRating rating1 = ProductRating.builder()
                .user(user)
                .product(product)
                .star(productRatingDto.getStar())
                .content(productRatingDto.getRatingContent())
                .date(instant.atZone(ZoneId.systemDefault()).toLocalDate())
                .build();

        ProductRating rating2 = ProductRating.builder()
                .user(user)
                .product(product)
                .star(productRatingDto.getStar())
                .content(productRatingDto.getRatingContent())
                .date(instant.atZone(ZoneId.systemDefault()).toLocalDate())
                .build();
        productRatingList = Arrays.asList(rating1, rating2);

    }

    @Test
    void addNewRatingTest_Success() {
        long id = 1L;
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(userRepository.findByUsername(productRatingDto.getUserName())).thenReturn(Optional.of(user));

        ratingService.addNewRating(productRatingDto);

        verify(ratingRepository).save(any(ProductRating.class));

    }

    @Test
    void addNewRating_FailToLoadProduct() {
        long id = 1L;
        when(productRepository.findById(id)).thenThrow(ProductNotFoundException.class);
        when(userRepository.findByUsername(productRatingDto.getUserName())).thenReturn(Optional.of(user));

        assertThrows(ProductNotFoundException.class, () -> ratingService.addNewRating(productRatingDto));
    }

    @Test
    void addNewRating_FailToLoadUser() {
        long id = 1L;
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(userRepository.findByUsername(productRatingDto.getUserName())).thenThrow(UsernameNotFoundException.class);

        assertThrows(UsernameNotFoundException.class, () -> ratingService.addNewRating(productRatingDto));
    }

    @Test
    void testGetAllProductRating() {
        long productId = 1L;
        int p = 0;

        Pageable pageable = PageRequest.of(p, 2);
        Page<ProductRating> page = new PageImpl(productRatingList, pageable, productRatingList.size());

        when(ratingRepository.findAllByProductId(eq(productId), any(PageRequest.class)))
                .thenReturn(page);

        RatingResponse ratingResponse = ratingService.getAllProductRating(productId, p);


        assertEquals(2, page.getTotalElements());
        assertEquals(0, ratingResponse.getPageNumber());
        assertEquals(2, ratingResponse.getPageSize());
        assertEquals(2, ratingResponse.getTotalElement());
        assertEquals(1, ratingResponse.getTotalPage());
    }

    @Test
    void testGetStarRatingOverview() {
        Long productId = 1L;

        // Mock the repository method for different star ratings
        when(ratingRepository.findStarOverviewByProductId(productId, 5)).thenReturn(10);
        when(ratingRepository.findStarOverviewByProductId(productId, 4)).thenReturn(8);
        when(ratingRepository.findStarOverviewByProductId(productId, 3)).thenReturn(6);
        when(ratingRepository.findStarOverviewByProductId(productId, 2)).thenReturn(4);
        when(ratingRepository.findStarOverviewByProductId(productId, 1)).thenReturn(2);

        List<StarRatingOverviewDto> result = ratingService.getStarRatingOverview(productId);

        List<StarRatingOverviewDto> expected = new ArrayList<>();
        expected.add(StarRatingOverviewDto.builder().label("5").quantity(10).build());
        expected.add(StarRatingOverviewDto.builder().label("4").quantity(8).build());
        expected.add(StarRatingOverviewDto.builder().label("3").quantity(6).build());
        expected.add(StarRatingOverviewDto.builder().label("2").quantity(4).build());
        expected.add(StarRatingOverviewDto.builder().label("1").quantity(2).build());

        assertEquals(eq(expected), eq(result));
    }

    @Test
    void testGetAverageStar() {
        Long id = 1L;
        when(ratingRepository.finAverageStarByProductId(id)).thenReturn(4.5f);

        float averageStar = ratingService.getAverageStar(id);

        assertEquals(4.5, averageStar);
    }

    @Test
    void testGetTotalRatingByProductId(){
        Long id = 1L;

        when(ratingRepository.countAllByProductId(id)).thenReturn(5L);

        long totalRating = ratingService.getTotalRating(id);

        assertEquals(5L, totalRating);
    }
}
