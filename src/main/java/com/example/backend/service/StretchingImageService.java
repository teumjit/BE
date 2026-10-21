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
                    "/images/stretching/0_bend_forward/00bend_forward.png"
            ),
            new StretchingImageResponse(
                    2L,
                    "앞으로 숙이기",
                    "등",
                    "/images/stretching/0_bend_forward/01bend_forward.png"
            ),

            new StretchingImageResponse(
                    3L,
                    "턱 힘 빼기",
                    "얼굴",
                    "/images/stretching/1_jaw_relax/10jaw_relax.png"
            ),
            new StretchingImageResponse(
                    4L,
                    "턱 힘 빼기",
                    "얼굴",
                    "/images/stretching/1_jaw_relax/11jaw_relax.png"
            ),

            new StretchingImageResponse(
                    5L,
                    "관자놀이 원 그리기",
                    "얼굴",
                    "/images/stretching/2_head_circle/20head_circle.png"
            ),
            new StretchingImageResponse(
                    6L,
                    "턱 힘 빼기",
                    "얼굴",
                    "/images/stretching/2_head_circle/21head_circle.png"
            ),
            
            new StretchingImageResponse(
                    7L,
                    "손목 원 돌리기",
                    "팔",
                    "/images/stretching/3_hand_circle/30hand_circle.png"
            ),
            new StretchingImageResponse(
                    8L,
                    "손목 원 돌리기",
                    "팔",
                    "/images/stretching/3_hand_circle/31hand_circle.png"
            ),

            new StretchingImageResponse(
                    9L,
                    "손목 위아래 움직이기",
                    "팔",
                    "/images/stretching/4_updown_hands/40updown_hands.png"
            ),
            new StretchingImageResponse(
                    10L,
                    "손목 위아래 움직이기",
                    "팔",
                    "/images/stretching/4_updown_hands/41updown_hands.png"
            ),
            new StretchingImageResponse(
                    11L,
                    "손목 위아래 움직이기",
                    "팔",
                    "/images/stretching/4_updown_hands/42updown_hands.png"
            ),

            new StretchingImageResponse(
                    12L,
                    "손바닥 뒤집기",
                    "팔",
                    "/images/stretching/5_flip_hand/50flip_hand.png"
            ),
            new StretchingImageResponse(
                    13L,
                    "손바닥 뒤집기",
                    "팔",
                    "/images/stretching/5_flip_hand/51flip_hand.png"
            ),

            new StretchingImageResponse(
                    14L,
                    "발가락 펴고 오므리기",
                    "발",
                    "/images/stretching/6_open_toe/60open_toe.png"
            ),
            new StretchingImageResponse(
                    15L,
                    "발가락 펴고 오므리기",
                    "발",
                    "/images/stretching/6_open_toe/61open_toe.png"
            ),

            new StretchingImageResponse(
                    16L,
                    "까치발 서기",
                    "다리",
                    "/images/stretching/7_point_toe/70point_toe.png"
            ),
            new StretchingImageResponse(
                    17L,
                    "까치발 서기",
                    "다리",
                    "/images/stretching/7_point_toe/71point_toe.png"
            ),

            new StretchingImageResponse(
                    18L,
                    "서서 등 둥글게 말기",
                    "등",
                    "/images/stretching/8_round_back/80round_back.png"
            ),
            new StretchingImageResponse(
                    19L,
                    "서서 등 둥글게 말기",
                    "등",
                    "/images/stretching/8_round_back/81round_back.png"
            ),
            
            new StretchingImageResponse(
                    20L,
                    "턱끝에서 귓불로 쓸기",
                    "얼굴",
                    "/images/stretching/9_jaw_to_ear/90jaw_to_ear.png"
            ),
            new StretchingImageResponse(
                    21L,
                    "턱끝에서 귓불로 쓸기",
                    "얼굴",
                    "/images/stretching/9_jaw_to_ear/91jaw_to_ear.png"
            ),
            new StretchingImageResponse(
                    23L,
                    "턱끝에서 귓불로 쓸기",
                    "얼굴",
                    "/images/stretching/9_jaw_to_ear/92jaw_to_ear.png"
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