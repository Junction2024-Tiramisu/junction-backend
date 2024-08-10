package com.example.junction.domain;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@DataJpaTest
@EnableJpaAuditing
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SpotRepositoryTest {

  @Autowired
  private SpotRepository spotRepository;

  @Test
  void findBetweenSpotsTest() {
    // given
    final double startLatitude = 37;
    final double endLatitude = 38;
    final double startLongitude = 126;
    final double endLongitude = 127;
    final LocalDateTime startCreatedAt = LocalDateTime.now().minusDays(1);
    final LocalDateTime endCreatedAt = LocalDateTime.now().plusDays(1);

    final List<Spot> expects = List.of(
        new Spot(37.5, 126.5, 0.998),
        new Spot(37.6, 126.6, 0.997),
        new Spot(37.7, 126.7, 0.996)
    );

    spotRepository.saveAll(expects);

    // when
    final List<Spot> actual = spotRepository.findBetweenSpots(
        startLatitude,
        endLatitude,
        startLongitude,
        endLongitude,
        startCreatedAt,
        endCreatedAt
    );

    // then
    assertThat(actual)
        .usingRecursiveComparison()
        .ignoringFields("id", "createdAt")
        .isEqualTo(expects);
  }
}
