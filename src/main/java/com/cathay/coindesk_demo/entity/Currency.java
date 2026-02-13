package com.cathay.coindesk_demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "currency")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @Column(name = "chinese_name", nullable = false, length = 50)
    private String chineseName;

    @Column(precision = 20, scale = 8)
    private BigDecimal rate; // 可為 null：匯率通常由 coindesk 即時提供
}
