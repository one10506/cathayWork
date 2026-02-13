package com.cathay.coindesk_demo.dao;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyRequest {
    private String code;
    private String chineseName;
    private BigDecimal rate;
}
