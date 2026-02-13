package com.cathay.coindesk_demo.controller;

import com.cathay.coindesk_demo.dao.CoindeskRawResponse;
import com.cathay.coindesk_demo.entity.Currency;
import com.cathay.coindesk_demo.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class CoindeskConvertApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyRepository currencyRepository;

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        currencyRepository.deleteAll();

        Currency usd = new Currency();
        usd.setCode("USD");
        usd.setChineseName("美元");
        currencyRepository.save(usd);
    }

    @Test
    void callConvertApiTest() throws Exception {

        CoindeskRawResponse raw = new CoindeskRawResponse();

        CoindeskRawResponse.Time time = new CoindeskRawResponse.Time();
        time.setUpdatedISO("2024-09-02T07:07:20+00:00");
        raw.setTime(time);

        Map<String, CoindeskRawResponse.Bpi> bpiMap = new HashMap<>();
        CoindeskRawResponse.Bpi bpi = new CoindeskRawResponse.Bpi();
        bpi.setCode("USD");
        bpi.setRate_float(30000.1234);
        bpi.setRate("30,000.1234");

        bpiMap.put("USD", bpi);
        raw.setBpi(bpiMap);

        when(restTemplate.getForObject(anyString(), eq(CoindeskRawResponse.class)))
                .thenReturn(raw);

        String response = mockMvc.perform(get("/coindesk/convert"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println("=== /coindesk/convert API 結果 ===");
        System.out.println(response);
    }
}