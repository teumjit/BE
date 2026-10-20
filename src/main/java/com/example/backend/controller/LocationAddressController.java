package com.example.backend.controller;

import com.example.backend.service.LocationAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/locationData")
@Tag(name = "Location Address", description = "GPS 좌표를 한국어 주소로 변환")
public class LocationAddressController {

    private final LocationAddressService locationAddressService;

    public LocationAddressController(
            LocationAddressService locationAddressService) {
        this.locationAddressService = locationAddressService;
    }

    @GetMapping("/address")
    @Operation(
        summary = "GPS 좌표로 주소 조회",
        description = "위도와 경도를 입력받아 한국어 주소를 반환합니다."
    )
    public Map<String, String> getAddress(
            @Parameter(description = "위도", example = "37.5665")
            @RequestParam double latitude,

            @Parameter(description = "경도", example = "126.9780")
            @RequestParam double longitude) {

        if (!Double.isFinite(latitude)
                || latitude < -90 || latitude > 90
                || !Double.isFinite(longitude)
                || longitude < -180 || longitude > 180) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "올바르지 않은 GPS 좌표입니다."
            );
        }

        String address =
                locationAddressService.getKoreanAddress(latitude, longitude);

        return Map.of("address", address);
    }
}