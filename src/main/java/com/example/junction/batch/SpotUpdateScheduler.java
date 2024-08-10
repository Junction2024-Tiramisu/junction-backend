package com.example.junction.batch;

import static java.util.stream.Collectors.toMap;

import com.example.junction.application.SpotCreateRequest;
import com.example.junction.application.SpotCreateService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpotUpdateScheduler {

  private static final String COMMA_DELIMITER = ",";
  private static final String LATITUDE_COLUMN = "latitude";
  private static final String LONGITUDE_COLUMN = "longitude";
  private static final String WEIGHT_COLUMN = "scaled_lightlux";

  private final ResourceLoader resourceLoader;
  private final SpotCreateService spotCreateService;

  @Scheduled(cron = "0 0 3 * * *") // 초 분 시 일 월 요일
  public void update() {
    final LocalDate today = LocalDate.now();
    final Resource resource = resourceLoader.getResource(getResourcePath(today));
    // TODO: 파일이 존재하지 않을 경우 일정 시간을 두고 재시도 처리
    if (!resource.exists()) {
      return;
    }
    // TODO: 가공되지 않은 CSV 파일 데이터를 가공해서 Spot을 저장하는 로직 구현
    final List<SpotCreateRequest> spotCreateRequests = readCsv(resource).stream()
        .map(row -> new SpotCreateRequest(
            Double.valueOf(row.get(LATITUDE_COLUMN)),
            Double.valueOf(row.get(LONGITUDE_COLUMN)),
            Double.valueOf(row.get(WEIGHT_COLUMN))
        ))
        .toList();
    spotCreateService.createSpots(spotCreateRequests);
  }

  private String getResourcePath(final LocalDate today) {
    return "classpath:data/" + today.getYear() + today.getMonth() + today.getDayOfMonth() + ".csv";
  }

  private List<Map<String, String>> readCsv(final Resource resource) {
    try (final BufferedReader reader = new BufferedReader(
        new InputStreamReader(resource.getInputStream()))) {
      final String[] headers = reader.readLine().split(COMMA_DELIMITER);
      return reader.lines()
          .map(row -> row.split(COMMA_DELIMITER))
          .map(
              row -> IntStream.range(0, row.length)
                  .boxed()
                  .collect(toMap(i -> headers[i], i -> row[i]))
          )
          .toList();
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
}
