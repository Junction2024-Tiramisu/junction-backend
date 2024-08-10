package com.example.junction.api;

import com.example.junction.application.SpotQueryRequest;
import com.example.junction.application.SpotQueryService;
import com.example.junction.application.SpotsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("spots")
@RequiredArgsConstructor
public class SpotApi {

  private final SpotQueryService spotQueryService;

  @GetMapping
  public SpotsResponse getSpots(
      final SpotQueryRequest request
  ) {
    return spotQueryService.getSpots(request);
  }
}
