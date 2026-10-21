package com.example.backend.service;

import com.example.backend.dto.StretchingImageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StretchingImageService {

    private final List<StretchingImageResponse> images = List.of(
            new StretchingImageResponse(
                    1L,
                    "앞으로 숙이기",
                    "등",
                    "/images/stretching/0_bend_forward/00bend_forward"
            ),
            new StretchingImageResponse(
                    2L,
                    "앞으로 숙이기",
                    "등",
                    "/images/stretching/0_bend_forward/01bend_forward"
            ),

            new StretchingImageResponse(
                    3L,
                    "턱 힘 빼기",
                    "얼굴",
                    "/images/stretching/1_jaw_relax/10jaw_relax"
            ),
            new StretchingImageResponse(
                    4L,
                    "턱 힘 빼기",
                    "얼굴",
                    "/images/stretching/1_jaw_relax/11jaw_relax"
            ),
            
            new StretchingImageResponse(
                    5L,
                    "손목 원 돌리기",
                    "팔",
                    "/images/stretching/3_hand_circle/30hand_circle"
            ),
            new StretchingImageResponse(
                    6L,
                    "손목 원 돌리기",
                    "팔",
                    "/images/stretching/3_hand_circle/31hand_circle"
            ),

            new StretchingImageResponse(
                    7L,
                    "손바닥 뒤집기",
                    "팔",
                    "/images/stretching/4_updown_hands/40updown_hands"
            ),
            new StretchingImageResponse(
                    8L,
                    "손바닥 뒤집기",
                    "팔",
                    "/images/stretching/4_updown_hands/41updown_hands"
            ),
            new StretchingImageResponse(
                    7L,
                    "손바닥 뒤집기",
                    "팔",
                    "/images/stretching/4_updown_hands/42updown_hands"
            ),

            new StretchingImageResponse(
                    9L,
                    "손바닥 뒤집기",
                    "팔",
                    "/images/stretching/5_flip_hand/50flip_hand"
            ),
            new StretchingImageResponse(
                    7L,
                    "손바닥 뒤집기",
                    "팔",
                    "/images/stretching/5_flip_hand/51flip_hand"
            ),


            

            new StretchingImageResponse(
                    20L,
                    "턱끝에서 귓불로 쓸기",
                    "얼굴",
                    "/images/stretching/9_jaw_to_ear/90jaw_to_ear"
            ),
            new StretchingImageResponse(
                    21L,
                    "턱끝에서 귓불로 쓸기",
                    "얼굴",
                    "/images/stretching/9_jaw_to_ear/91jaw_to_ear"
            ),
            new StretchingImageResponse(
                    23L,
                    "턱끝에서 귓불로 쓸기",
                    "얼굴",
                    "/images/stretching/9_jaw_to_ear/92jaw_to_ear"
            )
    );

    public List<StretchingImageResponse> getImages(String category) {

        if (category == null || category.isBlank()) {
            return images;
        }

        return images.stream()
                .filter(image ->
                        image.category().equalsIgnoreCase(category))
                .toList();
    }

    public StretchingImageResponse getImageById(Long id) {
        return images.stream()
                .filter(image -> image.id().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "해당 스트레칭 사진을 찾을 수 없습니다. ID: " + id
                        ));
    }
}