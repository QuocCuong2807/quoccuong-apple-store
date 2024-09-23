package com.springteam.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springteam.backend.dto.ProductRatingDto;
import com.springteam.backend.service.IRatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductRatingControllerTest {
    @MockBean
    private IRatingService ratingService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @BeforeEach
    void beforeEach(){

    }

    @Test
    @WithMockUser(username = "ad_quoccuong", authorities = {"ADMIN", "USER"})
    void testAddNewProductRating_Success() throws Exception {

        ProductRatingDto ratingDto = ProductRatingDto.builder()
                .star(5)
                .ratingContent("xịn")
                .productId(1L)
                .userName("ad_quoccuong")
                .date("1672531199000")
                .build();

        mockMvc.perform(post("/api/rating/new-rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingDto)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(ratingService).addNewRating(any(ProductRatingDto.class));

    }

    @Test
    @WithMockUser(username = "ad_quoccuong", authorities = {"ADMIN", "USER"})
    void testAddNewProductRating_Fail() throws Exception {
        ProductRatingDto invalidRatingDto = ProductRatingDto.builder()
                .star(0)
                .ratingContent("")
                .productId(null)
                .userName("")
                .date("")
                .build();

        mockMvc.perform(post("/api/rating/new-rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRatingDto )))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("vui lòng nhập đủ thông tin đánh giá"))
                .andReturn();
    }

    @Test
    void testAddNewProductRating_UnAuth() throws Exception {
        ProductRatingDto ratingDto = ProductRatingDto.builder()
                .star(5)
                .ratingContent("xịn")
                .productId(1L)
                .userName("ad_quoccuong")
                .date("1672531199000")
                .build();

        mockMvc.perform(post("/api/rating/new-rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingDto)))
                .andExpect(status().isForbidden())
                .andReturn();
    }

}
