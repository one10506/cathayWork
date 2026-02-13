package com.cathay.coindesk_demo.service;

import com.cathay.coindesk_demo.dao.CoindeskConvertedResponse;
import com.cathay.coindesk_demo.dao.CoindeskRawResponse;
import com.cathay.coindesk_demo.entity.Currency;
import com.cathay.coindesk_demo.repository.CurrencyRepository;
import com.cathay.coindesk_demo.service.CoindeskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class CoindeskServiceUnitTest {

    @Test
    void fetchAndConvertTestForRateFloat() {

        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        CurrencyRepository currencyRepository = Mockito.mock(CurrencyRepository.class);

        Currency usdDb = new Currency();
        usdDb.setCode("USD");
        usdDb.setChineseName("美元");

        when(currencyRepository.findByCode("USD")).thenReturn(Optional.of(usdDb));

        CoindeskRawResponse raw = new CoindeskRawResponse();
        CoindeskRawResponse.Time time = new CoindeskRawResponse.Time();
        time.setUpdatedISO("2024-09-02T07:07:20+00:00");
        raw.setTime(time);

        Map<String, CoindeskRawResponse.Bpi> bpi = new HashMap<>();
        CoindeskRawResponse.Bpi usd = new CoindeskRawResponse.Bpi();
        usd.setCode("USD");
        usd.setRate_float(30000.1234);
        usd.setRate("30,000.1234");
        bpi.put("USD", usd);
        raw.setBpi(bpi);

        when(restTemplate.getForObject(anyString(), eq(CoindeskRawResponse.class)))
                .thenReturn(raw);

        CoindeskService service = new CoindeskService(restTemplate, currencyRepository);

        CoindeskConvertedResponse resp = service.fetchAndConvert();

        assertEquals("2024/09/02 07:07:20", resp.getUpdateTime());
        assertNotNull(resp.getCurrencies());
        assertEquals(1, resp.getCurrencies().size());

        CoindeskConvertedResponse.Item item = resp.getCurrencies().get(0);
        assertEquals("USD", item.getCode());
        assertEquals("美元", item.getChineseName());
        //走rate_float
        assertEquals(new BigDecimal("30000.1234"), item.getRate());

        System.out.println("=== UnitTest fetchAndConvert (rate_float) result ===");
        System.out.println(resp);
    }

    @Test
    void fetchAndConvertTestForNotRateFloat() {

        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        CurrencyRepository currencyRepository = Mockito.mock(CurrencyRepository.class);

        Currency usdDb = new Currency();
        usdDb.setCode("USD");
        usdDb.setChineseName("美元");

        when(currencyRepository.findByCode("USD")).thenReturn(Optional.of(usdDb));

        //coindesk raw：rate_float = null→改走rate字串去逗號
        CoindeskRawResponse raw = new CoindeskRawResponse();
        CoindeskRawResponse.Time time = new CoindeskRawResponse.Time();
        time.setUpdatedISO("2024-09-02T07:07:20+00:00");
        raw.setTime(time);

        Map<String, CoindeskRawResponse.Bpi> bpi = new HashMap<>();
        CoindeskRawResponse.Bpi usd = new CoindeskRawResponse.Bpi();
        usd.setCode("USD");
        usd.setRate_float(null);
        usd.setRate("30,000.1234");
        bpi.put("USD", usd);
        raw.setBpi(bpi);

        when(restTemplate.getForObject(anyString(), eq(CoindeskRawResponse.class)))
                .thenReturn(raw);

        CoindeskService service = new CoindeskService(restTemplate, currencyRepository);

        CoindeskConvertedResponse resp = service.fetchAndConvert();

        assertEquals("2024/09/02 07:07:20", resp.getUpdateTime());
        assertEquals(1, resp.getCurrencies().size());

        CoindeskConvertedResponse.Item item = resp.getCurrencies().get(0);
        assertEquals("USD", item.getCode());
        assertEquals("美元", item.getChineseName());
        //走rate字串去逗號
        assertEquals(new BigDecimal("30000.1234"), item.getRate());

        System.out.println("=== UnitTest fetchAndConvert (rate string) result ===");
        System.out.println(resp);
    }
}