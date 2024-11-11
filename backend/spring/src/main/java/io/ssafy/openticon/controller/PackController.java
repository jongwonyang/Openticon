package io.ssafy.openticon.controller;

import io.ssafy.openticon.controller.request.EmoticonUploadRequestDto;
import io.ssafy.openticon.controller.request.ReportPackRequestDto;
import io.ssafy.openticon.controller.response.*;
import io.ssafy.openticon.dto.EmoticonPack;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.repository.MemberRepository;
import io.ssafy.openticon.service.MemberService;
import io.ssafy.openticon.service.PackService;
import io.ssafy.openticon.service.ReportHistoryService;
import io.ssafy.openticon.service.SafeSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.server.ResponseStatusException;

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
    private final MemberRepository memberRepository;

    public PackController(PackService packService, ReportHistoryService reportHistoryService, MemberService memberService, MemberRepository memberRepository){
        this.packService=packService;
        this.reportHistoryService = reportHistoryService;
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "작가가 이모티콘팩을 등록합니다.")
    public ResponseEntity<EmoticonPackResponseDto> uploadEmoticon(
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

        EmoticonPackResponseDto emoticonPackResponseDto = packService.emoticonPackUpload(emoticonPack);
//        emoticonPackResponseDto
//        String shareUrl=baseUrl+"/api/v1/emoticonpacks/info/"+shareLink;
//        UploadEmoticonResponseDto uploadEmoticonResponseDto=new UploadEmoticonResponseDto(shareUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(emoticonPackResponseDto);
    }

    @GetMapping("/info/{uuid}")
    @Operation(summary = "비공개 이모티콘팩 경로에 접근합니다.")
    public ResponseEntity<PackInfoResponseDto> viewPackInfo(@AuthenticationPrincipal UserDetails userDetails,
                                                            @PathVariable("uuid") String uuid,
                                                            HttpServletRequest request) throws AuthenticationException {

        //String email=userDetails.getUsername();

        return ResponseEntity.status(HttpStatus.OK).body(packService.getPackInfo(uuid,userDetails,request.getRemoteAddr()));

    }

    @GetMapping("/info")
    @Operation(summary = "공개 이모티콘팩 경로에 접근합니다.")
    public ResponseEntity<PackInfoResponseDto> viewPackInfoPublic(@AuthenticationPrincipal UserDetails userDetails,
                                                                  @RequestParam("emoticonPackId") String packId,
                                                                  HttpServletRequest request){

        return ResponseEntity.status(HttpStatus.OK).body(packService.getPackInfoByPackId(packId, userDetails, request.getRemoteAddr()));

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

    @GetMapping("mylist")
    @Operation(summary = "내가 만든 이모티콘 팩 조회")
    public ResponseEntity<Page<EmoticonPackResponseDto>> myListPack(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        MemberEntity member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return ResponseEntity.status(HttpStatus.OK).body(packService.myPackList(member, pageable));
    }

    @GetMapping("download-init")
    @Operation(summary = "다운로드 수 초기 세팅")
    public ResponseEntity<?> emoticonPackDownloadInit(){
        packService.downloadInit();
        return ResponseEntity.ok().body("다했어요");
    }

    @DeleteMapping("delete")
    @Operation(summary = "이모티콘 팩 삭제")
    public ResponseEntity<PackResponseDto> emoticonPackDeleted(@AuthenticationPrincipal UserDetails userDetails,
                                                               @RequestBody Long emoticonPackId){
        MemberEntity member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));
        return ResponseEntity.ok().body(packService.deleted(member, emoticonPackId));
    }
}
