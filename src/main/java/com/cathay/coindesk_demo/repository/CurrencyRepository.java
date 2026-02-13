package com.cathay.coindesk_demo.repository;

import com.cathay.coindesk_demo.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Optional<Currency> findByCode(String code);
    boolean existsByCode(String code);
}
