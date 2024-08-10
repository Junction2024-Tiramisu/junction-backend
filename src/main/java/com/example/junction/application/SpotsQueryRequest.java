package com.example.junction.application;

import org.springframework.web.bind.annotation.RequestParam;

public record SpotsQueryRequest(
    @RequestParam Double startLatitude,
    @RequestParam Double startLongitude,
    @RequestParam Double endLatitude,
    @RequestParam Double endLongitude
) {

}
