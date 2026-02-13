package com.cathay.coindesk_demo.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyResponse {
    private Long id;
    private String code;
    private String chineseName;
    private BigDecimal rate;
}
