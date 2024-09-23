package com.springteam.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springteam.backend.service.IDashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DashboardControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IDashboardService dashboardService;


    @BeforeEach
    void beforeEach(){}

    @Test
    @WithMockUser(username = "ad_quoccuong", authorities = {"ADMIN"})
    void getOrderQuantityTest_Success() throws Exception {
        mockMvc.perform(get("/api/dashboard/order-quantity"))
                .andExpect(status().isOk())
                .andReturn();

        verify(dashboardService).getOrderQuantity();
    }

    @Test
    void getOrderQuantityTest_FailAuth() throws Exception {
        mockMvc.perform(get("/api/dashboard/order-quantity"))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "ad_quoccuong", authorities = {"ADMIN"})
    void getProductQuantity_Success() throws Exception {
        mockMvc.perform(get("/api/dashboard/product-quantity"))
                .andExpect(status().isOk())
                .andReturn();

        verify(dashboardService).getProductQuantity();
    }

    @Test
    void getProductQuantity_FailAuth() throws Exception {
        mockMvc.perform(get("/api/dashboard/product-quantity"))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "ad_quoccuong", authorities = {"ADMIN"})
    void getTotalRevenue_Success() throws Exception {
        mockMvc.perform(get("/api/dashboard/revenue"))
                .andExpect(status().isOk())
                .andReturn();

        verify(dashboardService).getTotalRevenue();
    }

    @Test
    void getTotalRevenue_FailAuth() throws Exception {
        mockMvc.perform(get("/api/dashboard/revenue"))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "ad_quoccuong", authorities = {"ADMIN"})
    void getTotalIncomesAnalysis_Success() throws Exception {
        String filter = "TOTAL_REVENUE";
        mockMvc.perform(get("/api/dashboard/total-incomes-analysis?filter=TOTAL_REVENUE"))
                .andExpect(status().isOk())
                .andReturn();

        verify(dashboardService).getRevenueDataChartByCategory(filter);
    }

    @Test
    void getTotalIncomesAnalysis_FailAuth() throws Exception {
        mockMvc.perform(get("/api/dashboard/total-incomes-analysis?filter=TOTAL_REVENUE"))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}
