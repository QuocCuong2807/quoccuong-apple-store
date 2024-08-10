package com.springteam.backend.controller;

import com.springteam.backend.dto.ProductRatingDto;
import com.springteam.backend.dto.RatingResponse;
import com.springteam.backend.dto.StarRatingOverviewDto;
import com.springteam.backend.service.IRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/rating")
public class ProductRatingController {
    private IRatingService ratingService;

    @Autowired
    public ProductRatingController(IRatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/new-rating")
    public ResponseEntity<String> addNewProductRating(@RequestBody ProductRatingDto productRatingDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>("vui lòng nhập đủ thông tin đánh giá", HttpStatus.BAD_REQUEST);

        ratingService.addNewRating(productRatingDto);
        return new ResponseEntity<>("Đã thêm đánh giá", HttpStatus.CREATED);
    }

    @GetMapping("/product-rating")
    public ResponseEntity<RatingResponse> getSpecificProductRating(@RequestParam String productId, @RequestParam Optional<Integer> p){
        RatingResponse ratingResponse = ratingService.getAllProductRating(Long.valueOf(productId), p.orElse(0));
        return new ResponseEntity<>(ratingResponse, HttpStatus.OK);
    }

    @GetMapping("/total-rating")
    public ResponseEntity<Long> getTotalRating(@RequestParam String productId){
        long totalRating = ratingService.getTotalRating(Long.valueOf(productId));
        return new ResponseEntity<>(totalRating, HttpStatus.OK);
    }

    @GetMapping("/average-star")
    public ResponseEntity<Float> getAverageStarBySpecificProduct(@RequestParam String productId){
        Float star = ratingService.getAverageStar(Long.valueOf(productId));
        return new ResponseEntity<>(star, HttpStatus.OK);
    }

    @GetMapping("/star-overview")
    public ResponseEntity<List<StarRatingOverviewDto>> getStarOverviewBySpecificProduct(@RequestParam String productId){
        List<StarRatingOverviewDto> starRatingOverviewDtos = ratingService.getStarRatingOverview(Long.valueOf(productId));
        return new ResponseEntity<>(starRatingOverviewDtos, HttpStatus.OK);
    }
}
