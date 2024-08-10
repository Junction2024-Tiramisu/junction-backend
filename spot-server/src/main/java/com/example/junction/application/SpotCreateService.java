package com.example.junction.application;

import com.example.junction.domain.Spot;
import com.example.junction.domain.SpotRepository;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SpotCreateService {

  private final SpotRepository spotRepository;

  public void createSpots(final List<SpotCreateRequest> requests) {
    final List<Spot> spots = requests.stream()
        .map(request -> new Spot(
            request.latitude(),
            request.longitude(),
            request.weight())
        )
        .toList();
    spotRepository.saveAll(spots);
  }

  public boolean isUpdatedToday() {
    final LocalDateTime startOfDate = LocalDateTime.now().with(LocalTime.MIN);
    final LocalDateTime endOfDate = LocalDateTime.now().with(LocalTime.MAX);
    return spotRepository.existsByCreatedAtBetween(startOfDate, endOfDate);
  }
}
