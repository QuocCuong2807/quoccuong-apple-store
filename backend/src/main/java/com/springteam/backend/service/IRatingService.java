package com.springteam.backend.service;

import com.springteam.backend.dto.ProductRatingDto;
import com.springteam.backend.dto.RatingResponse;
import com.springteam.backend.dto.StarRatingOverviewDto;

import java.util.List;

public interface IRatingService {
    void addNewRating(ProductRatingDto productRatingDto);
    RatingResponse getAllProductRating(Long productId, int p);
    List<StarRatingOverviewDto> getStarRatingOverview(Long productId);
    float getAverageStar(Long productId);
    long getTotalRating(Long productId);
}
