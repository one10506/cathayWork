package com.cathay.coindesk_demo.controller;

import com.cathay.coindesk_demo.dao.CoindeskRawResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CoindeskRawApiTest {

    @Autowired MockMvc mockMvc;

    @MockBean RestTemplate restTemplate;

    @Test
    void callCoindeskRawApiTest() throws Exception {

        //mock外部coindesk回傳
        CoindeskRawResponse mock = new CoindeskRawResponse();

        CoindeskRawResponse.Time time = new CoindeskRawResponse.Time();
        time.setUpdated("Sep 2, 2024 07:07:20 UTC");
        time.setUpdatedISO("2024-09-02T07:07:20+00:00");
        time.setUpdateduk("Sep 2, 2024 at 08:07 BST");
        mock.setTime(time);
        mock.setDisclaimer("just for test");
        mock.setChartName("Bitcoin");

        Map<String, CoindeskRawResponse.Bpi> bpiMap = new HashMap<>();

        CoindeskRawResponse.Bpi usd = new CoindeskRawResponse.Bpi();
        usd.setCode("USD");
        usd.setRate("30,000.1234");
        usd.setRate_float(30000.1234);
        usd.setSymbol("test");
        usd.setDescription("test");
        bpiMap.put("USD", usd);

        mock.setBpi(bpiMap);

        when(restTemplate.getForObject(anyString(), eq(CoindeskRawResponse.class)))
                .thenReturn(mock);

        String body = mockMvc.perform(get("/coindesk/raw"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(java.nio.charset.StandardCharsets.UTF_8);

        System.out.println("=== API /coindesk/raw response ===");
        System.out.println(body);
    }
}