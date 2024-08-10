package com.example.junction.application;

import com.example.junction.domain.Spot;
import java.util.List;

public record SpotsResponse(
    List<SpotResponse> spots
) {

  public record SpotResponse(
      Double latitude,
      Double longitude,
      Double weight
  ) {

    public static SpotResponse from(final Spot spot) {
      return new SpotResponse(
          spot.getLatitude(),
          spot.getLongitude(),
          spot.getWeight()
      );
    }
  }
}
