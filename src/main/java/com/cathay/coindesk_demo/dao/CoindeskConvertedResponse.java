package com.cathay.coindesk_demo.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoindeskConvertedResponse {
    private String updateTime; // yyyy/MM/dd HH:mm:ss
    private List<Item> currencies;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private String code;
        private String chineseName;
        private BigDecimal rate;
    }
}
