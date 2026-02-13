package com.cathay.coindesk_demo.service;

import com.cathay.coindesk_demo.dao.CoindeskConvertedResponse;
import com.cathay.coindesk_demo.dao.CoindeskRawResponse;
import com.cathay.coindesk_demo.entity.Currency;
import com.cathay.coindesk_demo.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CoindeskService {

    private static final String COINDESK_URL = "https://kengp3.github.io/blog/coindesk.json";
    private static final DateTimeFormatter OUT_FMT = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    private final RestTemplate restTemplate;
    private final CurrencyRepository currencyRepository;

    private CoindeskConvertedResponse coindeskConvertedResponse;

    public CoindeskRawResponse fetchRaw() {
        return restTemplate.getForObject(COINDESK_URL, CoindeskRawResponse.class);
    }

    public CoindeskConvertedResponse fetchAndConvert() {
        CoindeskRawResponse raw = fetchRaw();
        String updateTime = OffsetDateTime.parse(raw.getTime().getUpdatedISO()).format(OUT_FMT);

        CoindeskConvertedResponse response = new CoindeskConvertedResponse();
        response.setUpdateTime(updateTime);

        List<CoindeskConvertedResponse.Item> items = new ArrayList<>();

        for (CoindeskRawResponse.Bpi bpi : raw.getBpi().values()) {
            Optional<Currency> currency = currencyRepository.findByCode(bpi.getCode());
            if (currency.isPresent()) {
                CoindeskConvertedResponse.Item item = new CoindeskConvertedResponse.Item();
                item.setCode(currency.get().getCode());
                item.setChineseName(currency.get().getChineseName());
                item.setRate(toBigDecimalRate(raw, currency.get()));
                items.add(item);
            }
        }
        response.setCurrencies(items);

        return response;
    }

    private BigDecimal toBigDecimalRate(CoindeskRawResponse raw, Currency currency) {
        if (raw.getBpi() == null)
            return null;
        CoindeskRawResponse.Bpi bpi = raw.getBpi().get(currency.getCode());
        if (bpi == null)
            return null;

        if (bpi.getRate_float() != null)
            return BigDecimal.valueOf(bpi.getRate_float());

        String rateStr = bpi.getRate();
        if (rateStr == null)
            return null;
        return new BigDecimal(rateStr.replace(",", ""));
    }
}
