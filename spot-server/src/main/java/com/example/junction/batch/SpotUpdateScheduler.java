package com.example.junction.batch;

import static java.util.stream.Collectors.toMap;

import com.example.junction.application.SpotCreateRequest;
import com.example.junction.application.SpotCreateService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpotUpdateScheduler {

  private static final String COMMA_DELIMITER = ",";
  private static final String LATITUDE_COLUMN = "latitude";
  private static final String LONGITUDE_COLUMN = "longitude";
  private static final String WEIGHT_COLUMN = "weight";

  private final SpotCreateService spotCreateService;
  @Value("${spot.update.url}")
  private String updateUrl;

  @Scheduled(cron = "0 0 * * * *") // 초 분 시 일 월 요일
  public void update() {
    if (spotCreateService.isUpdatedToday()) {
      return;
    }
    try {
      final URL url = new URL(updateUrl + getToday() + ".csv");
      final URLConnection connection = url.openConnection();
      final List<Map<String, String>> rows = readCsv(connection.getInputStream());
      final List<SpotCreateRequest> createRequests = rows.stream()
          .map(row -> new SpotCreateRequest(
              Double.parseDouble(row.get(LATITUDE_COLUMN)),
              Double.parseDouble(row.get(LONGITUDE_COLUMN)),
              Double.parseDouble(row.get(WEIGHT_COLUMN))
          ))
          .toList();
      spotCreateService.createSpots(createRequests);
    } catch (final IOException e) {
      throw new RuntimeException("Failed to connect to the update server: ", e);
    }
  }

  private String getToday() {
    final LocalDate today = LocalDate.now();
    return String.format("%04d%02d%02d", today.getYear(), today.getMonthValue(),
        today.getDayOfMonth());
  }

  private List<Map<String, String>> readCsv(final InputStream inputStream) {
    final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
    try (final BufferedReader reader = new BufferedReader(inputStreamReader)) {
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
      throw new RuntimeException("Failed to read csv file: ", e);
    }
  }
}
