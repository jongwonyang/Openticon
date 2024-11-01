package io.ssafy.openticon.controller;

import io.ssafy.openticon.controller.request.EmoticonUploadRequestDto;
import io.ssafy.openticon.controller.response.EmoticonPackResponseDto;
import io.ssafy.openticon.controller.response.PackDownloadResponseDto;
import io.ssafy.openticon.controller.response.PackInfoResponseDto;
import io.ssafy.openticon.controller.response.UploadEmoticonResponseDto;
import io.ssafy.openticon.dto.EmoticonPack;
import io.ssafy.openticon.service.PackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.sasl.AuthenticationException;
import java.util.List;

@RestController
@RequestMapping("/emoticonpacks")
@Tag(name="이모티콘팩")
public class PackController {

    @Value("${spring.base-url}")
    private String baseUrl;

    private final PackService packService;

    public PackController(PackService packService){
        this.packService=packService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "작가가 이모티콘팩을 등록합니다.")
    public ResponseEntity<UploadEmoticonResponseDto> uploadEmoticon(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "이모티콘 팩 정보", required = true)
            @RequestPart("packInfo") EmoticonUploadRequestDto emoticonUploadRequest,
            @Parameter(description = "이모티콘 팩 대표 이미지", required = true)
            @RequestPart("thumbnail_img") MultipartFile thumbnailImg,
            @Parameter(description = "이모티콘 팩 리스트 이미지", required = true)
            @RequestPart("list_img") MultipartFile listImg,
            @Parameter(description = "이모티콘 이미지", required = true)
            @RequestPart("emoticons") List<MultipartFile> emoticons
    ){
        EmoticonPack emoticonPack=new EmoticonPack(emoticonUploadRequest,userDetails.getUsername());

        emoticonPack.setImages(thumbnailImg, listImg, emoticons);

        String shareLink=packService.emoticonPackUpload(emoticonPack);
        String shareUrl=baseUrl+"/api/v1/emoticonpacks/info/"+shareLink;
        UploadEmoticonResponseDto uploadEmoticonResponseDto=new UploadEmoticonResponseDto(shareUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(uploadEmoticonResponseDto);
    }

    @GetMapping("/info/{uuid}")
    @Operation(summary = "비공개 이모티콘팩 경로에 접근합니다.")
    public ResponseEntity<PackInfoResponseDto> viewPackInfo(@AuthenticationPrincipal UserDetails userDetails,
                                                            @PathVariable("uuid") String uuid) throws AuthenticationException {

        String email=userDetails.getUsername();

        return ResponseEntity.status(HttpStatus.OK).body(packService.getPackInfo(uuid,email));

    }

    @GetMapping("/info")
    @Operation(summary = "공개 이모티콘팩 경로에 접근합니다.")
    public ResponseEntity<PackInfoResponseDto> viewPackInfoPublic(@AuthenticationPrincipal UserDetails userDetails,
                                                                  @RequestParam("emoticonPackId") String packId){

        return ResponseEntity.status(HttpStatus.OK).body(packService.getPackInfoByPackId(packId));

    }

    @GetMapping("/search")
    @Operation(summary = "이모티콘 팩을 검색합니다.")
    public ResponseEntity<Page<EmoticonPackResponseDto>> searchEmoticonPacks(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "title") String type, // title, tag, author
            @RequestParam(defaultValue = "new") String sort, // new, most
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, getSort(sort));
        Page<EmoticonPackResponseDto> results = packService.search(query, type, pageable);
        return ResponseEntity.ok(results);
    }

    private Sort getSort(String sort) {
        if (sort.equalsIgnoreCase("new")) {
            return Sort.by("createdAt").descending(); // 최신순 정렬 (내림차순)
        }
        else if (sort.equalsIgnoreCase("most")) {
            return Sort.by("view").descending(); // 인기순 정렬 (내림차순)
        }
        return Sort.unsorted(); // 기본 정렬 없음
    }

    @GetMapping("/download")
    @Operation(summary = "이모티콘 팩을 다운로드합니다.")
    public ResponseEntity<PackDownloadResponseDto> downloadPack(@RequestParam("emoticonPackId")String packId,
                                                                @AuthenticationPrincipal UserDetails userDetails){
        String email=userDetails.getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(packService.downloadPack(email,Long.parseLong(packId)));
    }
}
