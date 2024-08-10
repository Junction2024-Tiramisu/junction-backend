package com.example.junction.api;

import com.example.junction.application.SpotsQueryRequest;
import com.example.junction.application.SpotsQueryService;
import com.example.junction.application.SpotsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("spots")
@RequiredArgsConstructor
public class SpotApi {

  private final SpotsQueryService spotsQueryService;

  @GetMapping
  public SpotsResponse getSpots(
      final SpotsQueryRequest request
  ) {
    return spotsQueryService.getSpots(request);
  }
}
