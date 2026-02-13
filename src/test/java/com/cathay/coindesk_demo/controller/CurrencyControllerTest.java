package com.cathay.coindesk_demo.controller;

import com.cathay.coindesk_demo.dao.CurrencyRequest;
import com.cathay.coindesk_demo.dao.QueryCurrencyResponse;
import com.cathay.coindesk_demo.service.CurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyController.class)
class CurrencyControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean CurrencyService currencyService;

    @Test
    //查詢by code
    void getCodeTest() throws Exception {
        Mockito.when(currencyService.getCurrency("USD"))
                .thenReturn(new QueryCurrencyResponse(1L, "USD", "美元", new BigDecimal("32.50")));

        MvcResult result = mockMvc.perform(get("/currency/USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("USD"))
                .andExpect(jsonPath("$.chineseName").value("美元"))
                .andExpect(jsonPath("$.rate").value(32.50))
                .andReturn();

        System.out.println("GET /currency/USD => " + result.getResponse().getContentAsString());
    }

    @Test
    //查詢全部
    void listCurrencyTest() throws Exception {
        Mockito.when(currencyService.listCurrecny()).thenReturn(Arrays.asList(
                new QueryCurrencyResponse(1L, "USD", "美元", new BigDecimal("32.50")),
                new QueryCurrencyResponse(2L, "JPY", "日圓", new BigDecimal("0.22"))
        ));

        MvcResult result = mockMvc.perform(get("/currency"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("USD"))
                .andExpect(jsonPath("$[1].code").value("JPY"))
                .andReturn();

        System.out.println("GET /currency => " + result.getResponse().getContentAsString());
    }

    @Test
    //新增
    void createCurrencyTest() throws Exception {
        CurrencyRequest req = new CurrencyRequest();
        req.setCode("EUR");
        req.setChineseName("歐元");
        req.setRate(new BigDecimal("35.10"));

        mockMvc.perform(post("/currency")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        Mockito.verify(currencyService).createCurrency(any(CurrencyRequest.class));
    }

    @Test
    //修改
    void updateCurrencyTest() throws Exception {
        CurrencyRequest req = new CurrencyRequest();
        req.setCode("USD");
        req.setChineseName("美元(更新)");
        req.setRate(new BigDecimal("33.00"));

        mockMvc.perform(put("/currency")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        Mockito.verify(currencyService).updateCurrency(any(CurrencyRequest.class));
    }

    @Test
    //刪除
    void deleteCurrencyTest() throws Exception {
        CurrencyRequest req = new CurrencyRequest();
        req.setCode("USD");

        mockMvc.perform(delete("/currency")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        Mockito.verify(currencyService).deleteCurrency(eq(req));
    }
}
