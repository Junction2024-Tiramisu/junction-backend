package com.example.junction.application;

public record SpotCreateRequest(
    Double latitude,
    Double longitude,
    Double weight
) {

}
