package com.example.junction.application;

import com.example.junction.application.SpotsResponse.SpotResponse;
import com.example.junction.domain.Spot;
import com.example.junction.domain.SpotRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SpotQueryService {

  private final SpotRepository spotRepository;

  public SpotsResponse getSpots(final SpotQueryRequest request) {
    LocalDate targetDate = LocalDate.now();
    while (true) {
      final List<Spot> spots = getSpotsByDate(targetDate, request);
      if (!spots.isEmpty()) {
        return new SpotsResponse(spots.stream()
            .map(SpotResponse::from)
            .toList());
      }
      targetDate = targetDate.minusDays(1);
    }
  }

  public List<Spot> getSpotsByDate(final LocalDate date, final SpotQueryRequest request) {
    final LocalDateTime startOfDate = LocalDateTime.of(date, LocalTime.MIN);
    final LocalDateTime endOfDate = LocalDateTime.of(date, LocalTime.MAX);
    return spotRepository.findBetweenSpots(
        request.startLatitude(),
        request.endLatitude(),
        request.startLongitude(),
        request.endLongitude(),
        startOfDate,
        endOfDate
    );
  }
}
