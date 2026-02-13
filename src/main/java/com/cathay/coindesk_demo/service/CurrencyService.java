package com.cathay.coindesk_demo.service;

import com.cathay.coindesk_demo.dao.CurrencyRequest;
import com.cathay.coindesk_demo.dao.QueryCurrencyResponse;
import com.cathay.coindesk_demo.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.cathay.coindesk_demo.entity.Currency;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyService {


    private final CurrencyRepository currencyRepository;


    public QueryCurrencyResponse getCurrency(String code) {
        QueryCurrencyResponse currencyResponse = new QueryCurrencyResponse();
        Optional<Currency> currency = currencyRepository.findByCode(code);
        if (currency.isPresent()) {
            currencyResponse.setId(currency.get().getId());
            currencyResponse.setCode(currency.get().getCode());
            currencyResponse.setRate(currency.get().getRate());
            currencyResponse.setChineseName(currency.get().getChineseName());
        }
        return currencyResponse;
    }

    public List<QueryCurrencyResponse> listCurrecny() {
        List<QueryCurrencyResponse> currencyResponseList = new ArrayList<>();
        currencyRepository.findAll().forEach(currency -> {
            QueryCurrencyResponse currencyResponse = new QueryCurrencyResponse();
            currencyResponse.setId(currency.getId());
            currencyResponse.setCode(currency.getCode());
            currencyResponse.setRate(currency.getRate());
            currencyResponse.setChineseName(currency.getChineseName());
            currencyResponseList.add(currencyResponse);
        });
        return currencyResponseList;
    }

    @Transactional
    public void createCurrency(CurrencyRequest request) {
        if (currencyRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Currency code already exists: " + request.getCode());
        }
        Currency currency = new Currency();
        currency.setCode(request.getCode());
        currency.setRate(request.getRate());
        currency.setChineseName(request.getChineseName());
        currencyRepository.save(currency);
    }

    @Transactional
    public void updateCurrency(CurrencyRequest request) {
        Optional<Currency> currency = currencyRepository.findByCode(request.getCode());
        if (currency.isPresent()) {
            currency.get().setRate(request.getRate());
            currency.get().setChineseName(request.getChineseName());
            currencyRepository.save(currency.get());
        }
    }

    @Transactional
    public void deleteCurrency(CurrencyRequest request) {
        Optional<Currency> currency = currencyRepository.findByCode(request.getCode());
        if (currency.isPresent()) {
            currencyRepository.delete(currency.get());
        }
    }
}
