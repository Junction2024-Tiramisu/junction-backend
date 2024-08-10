package com.example.junction.domain;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpotRepository extends JpaRepository<Spot, Long> {

  @Query("""
      SELECT s
      FROM Spot s
      WHERE s.latitude >= :startLatitude
        AND s.latitude <= :endLatitude
        AND s.longitude >= :startLongitude
        AND s.longitude <= :endLongitude
        AND s.createdAt >= :startCreatedAt
        AND s.createdAt <= :endCreatedAt
      """)
  List<Spot> findBetweenSpots(
      Double startLatitude,
      Double endLatitude,
      Double startLongitude,
      Double endLongitude,
      LocalDateTime startCreatedAt,
      LocalDateTime endCreatedAt
  );
}
