package com.cathay.coindesk_demo.dao;

import lombok.Data;

import java.util.Map;

@Data
public class CoindeskRawResponse {
    private Time time;
    private String disclaimer;
    private String chartName;
    private Map<String, Bpi> bpi;

    @Data
    public static class Time {
        private String updated;    // e.g. "Sep 18, 2022 14:10:00 UTC"
        private String updatedISO; // e.g. "2022-09-18T14:10:00+00:00"
        private String updateduk;
    }

    @Data
    public static class Bpi {
        private String code;
        private String symbol;
        private String rate;       // e.g. "19,123.4567"
        private String description;
        private Double rate_float;
    }
}