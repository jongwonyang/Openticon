package io.ssafy.openticon.controller;

import io.ssafy.openticon.controller.request.EmoticonUploadRequestDto;
import io.ssafy.openticon.controller.request.ReportPackRequestDto;
import io.ssafy.openticon.controller.response.EmoticonPackResponseDto;
import io.ssafy.openticon.controller.response.PackDownloadResponseDto;
import io.ssafy.openticon.controller.response.PackInfoResponseDto;
import io.ssafy.openticon.controller.response.UploadEmoticonResponseDto;
import io.ssafy.openticon.dto.EmoticonPack;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.service.MemberService;
import io.ssafy.openticon.service.PackService;
import io.ssafy.openticon.service.ReportHistoryService;
import io.ssafy.openticon.service.SafeSearchService;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/emoticonpacks")
@Tag(name="이모티콘팩")
public class PackController {

    private final ReportHistoryService reportHistoryService;
    private final MemberService memberService;
    @Value("${spring.base-url}")
    private String baseUrl;

    private final PackService packService;

    public PackController(PackService packService, ReportHistoryService reportHistoryService, MemberService memberService){
        this.packService=packService;
        this.reportHistoryService = reportHistoryService;
        this.memberService = memberService;
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
    ) {
        System.out.println("Received request to upload emoticon pack");

        // 로그 추가 - UserDetails
        System.out.println("UserDetails Username: " + (userDetails != null ? userDetails.getUsername() : "null"));

        // 로그 추가 - EmoticonUploadRequestDto
        System.out.println("EmoticonUploadRequestDto: " + emoticonUploadRequest);

        // 로그 추가 - Thumbnail Image
        if (thumbnailImg != null) {
            System.out.println("Thumbnail Image - Original Filename: " + thumbnailImg.getOriginalFilename());
            System.out.println("Thumbnail Image - Size: " + thumbnailImg.getSize());
            System.out.println("Thumbnail Image - Content Type: " + thumbnailImg.getContentType());
        } else {
            System.out.println("Thumbnail Image is null");
        }

        // 로그 추가 - List Image
        if (listImg != null) {
            System.out.println("List Image - Original Filename: " + listImg.getOriginalFilename());
            System.out.println("List Image - Size: " + listImg.getSize());
            System.out.println("List Image - Content Type: " + listImg.getContentType());
        } else {
            System.out.println("List Image is null");
        }

        // 로그 추가 - Emoticons List
        System.out.println("Emoticons List Size: " + (emoticons != null ? emoticons.size() : "null"));
        if (emoticons != null) {
            for (int i = 0; i < emoticons.size(); i++) {
                MultipartFile emoticon = emoticons.get(i);
                System.out.println("Emoticon [" + i + "] - Original Filename: " + emoticon.getOriginalFilename());
                System.out.println("Emoticon [" + i + "] - Size: " + emoticon.getSize());
                System.out.println("Emoticon [" + i + "] - Content Type: " + emoticon.getContentType());
            }
        }

        EmoticonPack emoticonPack = new EmoticonPack(emoticonUploadRequest, userDetails.getUsername());
        emoticonPack.setImages(thumbnailImg, listImg, emoticons);

        // 로그 추가 - EmoticonPack 정보
        System.out.println("EmoticonPack Info - Username: " + emoticonPack.getUsername());
        System.out.println("EmoticonPack Info - Thumbnail Image: " + emoticonPack.getThumbnailImg().getOriginalFilename());
        System.out.println("EmoticonPack Info - List Image: " + emoticonPack.getListImg().getOriginalFilename());
        System.out.println("EmoticonPack Info - Emoticons Count: " + emoticonPack.getEmoticons().size());

        String shareLink = packService.emoticonPackUpload(emoticonPack);
        String shareUrl = baseUrl + "/api/v1/emoticonpacks/info/" + shareLink;
        UploadEmoticonResponseDto uploadEmoticonResponseDto = new UploadEmoticonResponseDto(shareUrl);

        System.out.println("Generated Share URL: " + shareUrl);

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

    @PostMapping("reports")
    public ResponseEntity<Void> reportPack(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody ReportPackRequestDto reportPackRequestDto){
        MemberEntity member=memberService.getMemberByEmail(userDetails.getUsername()).orElseThrow();
        reportHistoryService.report(reportPackRequestDto,member.getId());

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
