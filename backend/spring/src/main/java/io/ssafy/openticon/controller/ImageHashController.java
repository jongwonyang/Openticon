package io.ssafy.openticon.controller;

import io.ssafy.openticon.controller.response.ImageHashResponseDto;
import io.ssafy.openticon.service.ImageHashService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Tag(name="이모티콘팩 이미지 검색")
public class ImageHashController {

    private final ImageHashService imageHashService;

    public ImageHashController(ImageHashService imageHashService) {
        this.imageHashService = imageHashService;
    }

    @PostMapping("emoticonpacks/search/image")
    @Operation(summary = "이미지로 이모티콘을 검색합니다.(응답의 target이 찾고자 하는 이미지입니다.")
    public ResponseEntity<Page<ImageHashResponseDto>> searchEmoticonPackByImage(@RequestPart MultipartFile image,
                                                                                Pageable pageable) throws IOException {

        return ResponseEntity.status(HttpStatus.OK).body(imageHashService.searchImage(image,pageable));
    }
}
