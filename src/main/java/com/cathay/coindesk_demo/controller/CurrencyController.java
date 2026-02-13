package com.cathay.coindesk_demo.controller;

import com.cathay.coindesk_demo.dao.CurrencyRequest;
import com.cathay.coindesk_demo.dao.QueryCurrencyResponse;
import com.cathay.coindesk_demo.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("/{code}")
    public QueryCurrencyResponse getCode(@PathVariable String code) {
        return currencyService.getCurrency(code);
    }

    @GetMapping
    public List<QueryCurrencyResponse> listCurrency() {
        return currencyService.listCurrecny();
    }

    @PostMapping
    public void createCurrency(@RequestBody CurrencyRequest request) {
        currencyService.createCurrency(request);
    }

    @PutMapping
    public void updateCurrency(@RequestBody CurrencyRequest request) {
        currencyService.updateCurrency(request);
    }

    @DeleteMapping
    public void deleteCurrency(@RequestBody CurrencyRequest request) {
        currencyService.deleteCurrency(request);
    }
}
