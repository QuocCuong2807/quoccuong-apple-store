package com.springteam.backend.service.impl;

import com.springteam.backend.dto.ProductRatingDto;
import com.springteam.backend.dto.ProductResponse;
import com.springteam.backend.dto.RatingResponse;
import com.springteam.backend.dto.StarRatingOverviewDto;
import com.springteam.backend.entity.Product;
import com.springteam.backend.entity.ProductRating;
import com.springteam.backend.entity.UserEntity;
import com.springteam.backend.exception.ProductNotFoundException;
import com.springteam.backend.repository.IProductRepository;
import com.springteam.backend.repository.IRatingRepository;
import com.springteam.backend.repository.IUserRepository;
import com.springteam.backend.service.IRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingServiceImpl implements IRatingService {

    private IRatingRepository ratingRepository;
    private IUserRepository userRepository;
    private IProductRepository productRepository;

    @Autowired
    public RatingServiceImpl(IRatingRepository ratingRepository, IUserRepository userRepository, IProductRepository productRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void addNewRating(ProductRatingDto productRatingDto) {

        Product product = productRepository.findById(productRatingDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("cannot found product"));
        UserEntity user = userRepository.findByUsername(productRatingDto.getUserName())
                .orElseThrow(() -> new UsernameNotFoundException("cannot found user"));

        long timestamp = Long.valueOf(productRatingDto.getDate());
        Instant instant = Instant.ofEpochMilli(timestamp);

        ProductRating rating = ProductRating.builder()
                .content(productRatingDto.getRatingContent())
                .date(instant.atZone(ZoneId.systemDefault()).toLocalDate())
                .star(productRatingDto.getStar())
                .product(product)
                .user(user)
                .build();

        ratingRepository.save(rating);
    }

    @Override
    public RatingResponse getAllProductRating(Long productId, int p) {
        Page<ProductRating> page = ratingRepository.findAllByProductId(productId, PageRequest.of(p, 3));

        List<ProductRatingDto> ratingDtos = page.get().toList().stream()
                .map(rating -> ProductRatingDto.builder()
                        .productId(rating.getProduct().getId())
                        .ratingContent(rating.getContent())
                        .star(rating.getStar())
                        .userName(rating.getUser().getUsername())
                        .date(rating.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString())
                        .build())
                .collect(Collectors.toList());

        RatingResponse ratingResponse = RatingResponse.builder()
                .data(ratingDtos)
                .isFirstPage(page.isFirst())
                .isLastPage(page.isLast())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElement(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .build();

        return ratingResponse;
    }

    @Override
    public List<StarRatingOverviewDto> getStarRatingOverview(Long productId) {

        List<StarRatingOverviewDto> starRatingOverviewDtos = new ArrayList<>();

        for (int i = 5; i >= 1; i--) {
            starRatingOverviewDtos.add(StarRatingOverviewDto.builder()
                    .label(i + "")
                    .quantity(ratingRepository.findStarOverviewByProductId(productId, i))
                    .build());
        }

        return starRatingOverviewDtos;
    }

    @Override
    public float getAverageStar(Long productId) {
        return ratingRepository.finAverageStarByProductId(productId);
    }

    @Override
    public long getTotalRating(Long productId) {
        return ratingRepository.countAllByProductId(productId);
    }
}
