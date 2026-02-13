package com.cathay.coindesk_demo.controller;

import com.cathay.coindesk_demo.dao.CoindeskConvertedResponse;
import com.cathay.coindesk_demo.dao.CoindeskRawResponse;
import com.cathay.coindesk_demo.service.CoindeskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coindesk")
public class CoindeskController {

    private final CoindeskService coindeskService;

    @GetMapping("/raw")
    public CoindeskRawResponse raw() {
        return coindeskService.fetchRaw();
    }

    @GetMapping("/convert")
    public CoindeskConvertedResponse convert() {
        return coindeskService.fetchAndConvert();
    }
}
