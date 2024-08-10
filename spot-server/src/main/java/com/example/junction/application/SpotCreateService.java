package com.example.junction.application;

import com.example.junction.domain.Spot;
import com.example.junction.domain.SpotRepository;
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
}
