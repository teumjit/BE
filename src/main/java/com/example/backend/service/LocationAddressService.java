package com.example.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LocationAddressService {

    private final RestClient restClient;
    private final String apiKey;

    public LocationAddressService(
            @Value("${google.maps.api-key}") String apiKey) {
        this.restClient = RestClient.builder()
                .baseUrl("https://maps.googleapis.com")
                .build();
        this.apiKey = apiKey;
    }

    public String getKoreanAddress(double latitude, double longitude) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Google Maps API 키가 설정되지 않았습니다."
            );
        }

        try {
            JsonNode response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/maps/api/geocode/json")
                            .queryParam(
                                    "latlng",
                                    latitude + "," + longitude
                            )
                            .queryParam("language", "ko")
                            .queryParam("key", apiKey)
                            .build())
                    .retrieve()
                    .body(JsonNode.class);

            if (response == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY,
                        "Google API 응답이 비어 있습니다."
                );
            }

            String status = response.path("status").asText();

            if ("ZERO_RESULTS".equals(status)) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "해당 좌표의 주소를 찾을 수 없습니다."
                );
            }

            if (!"OK".equals(status)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY,
                        "Google 주소 조회에 실패했습니다: " + status
                );
            }

            JsonNode results = response.path("results");

            if (!results.isArray() || results.isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "주소 결과가 없습니다."
                );
            }

            return results.get(0)
                    .path("formatted_address")
                    .asText();

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            System.err.println(
                    "Google 주소 조회 실패: "
                            + e.getClass().getSimpleName()
                            + " - " + e.getMessage()
            );

            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Google 주소 조회 중 오류가 발생했습니다."
            );
        }
    }
}