package io.ssafy.openticon.controller;

import io.ssafy.openticon.controller.request.EmoticonUploadRequestDto;
import io.ssafy.openticon.controller.response.EmoticonPackResponseDto;
import io.ssafy.openticon.controller.response.UploadEmoticonResponseDto;
import io.ssafy.openticon.dto.EmoticonPack;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.service.PackService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/emoticonpacks")
public class PackController {

    @Value("${spring.base-url}")
    private String baseUrl;

    private final PackService packService;

    public PackController(PackService packService){
        this.packService=packService;
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadEmoticonResponseDto> uploadEmoticon(@AuthenticationPrincipal UserDetails userDetails,
                                                                    @RequestPart("packInfo") EmoticonUploadRequestDto emoticonUploadRequest,
                                                                    @RequestPart("thumbnail_img")MultipartFile thumbnailImg,
                                                                    @RequestPart("list_img") MultipartFile listImg,
                                                                    @RequestPart("emoticons")List<MultipartFile> emoticons
                                               ){

        EmoticonPack emoticonPack=new EmoticonPack(emoticonUploadRequest,userDetails.getUsername());
        emoticonPack.setImages(thumbnailImg, listImg, emoticons);

        String shareLink=packService.emoticonPackUpload(emoticonPack);
        String shareUrl=baseUrl+"/emoticonpacks/info/"+shareLink;
        UploadEmoticonResponseDto uploadEmoticonResponseDto=new UploadEmoticonResponseDto(shareUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(uploadEmoticonResponseDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<EmoticonPackResponseDto>> searchEmoticonPacks(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "new") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, getSort(sort));
        Page<EmoticonPackResponseDto> results = packService.search(query, type, pageable);
        return ResponseEntity.ok(results);
    }

    private Sort getSort(String sort) {
        // TODO: sort 기준 만들어야함
        if (sort.equalsIgnoreCase("new")) {
            return Sort.by("createdAt").descending(); // 최신순 정렬 (내림차순)
        }
//        else if (sort.equalsIgnoreCase("most")) {
//            return Sort.by("popularity").descending(); // 인기순 정렬 (내림차순)
//        }
        return Sort.unsorted(); // 기본 정렬 없음
    }
}
