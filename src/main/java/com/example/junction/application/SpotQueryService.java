package com.example.junction.application;

import com.example.junction.application.SpotsResponse.SpotResponse;
import com.example.junction.domain.SpotRepository;
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
    // TODO: Spot 데이터가 업데이트 되지 않았을 경우 전날 데이터를 보여주는 로직 구현
    final LocalDateTime startDateTime = LocalDateTime.now().with(LocalTime.MIN);
    final LocalDateTime endDateTime = LocalDateTime.now().with(LocalTime.MAX);
    final List<SpotResponse> spotResponses = spotRepository.findBetweenSpots(
            request.startLatitude(),
            request.endLatitude(),
            request.startLongitude(),
            request.endLongitude(),
            startDateTime,
            endDateTime
        ).stream()
        .map(SpotResponse::from)
        .toList();
    return new SpotsResponse(spotResponses);
  }
}
