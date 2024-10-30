package io.ssafy.openticon.controller;

import io.ssafy.openticon.controller.request.EmoticonUploadRequestDto;
import io.ssafy.openticon.controller.response.PackInfoResponseDto;
import io.ssafy.openticon.controller.response.UploadEmoticonResponseDto;
import io.ssafy.openticon.dto.EmoticonPack;
import io.ssafy.openticon.service.PackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
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

    @PostMapping("/upload")
    @Operation(summary = "작가가 이모티콘팩을 등록합니다.")
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
                                                                  @RequestParam("emoticonPackId") String packId) throws AuthenticationException {

        return ResponseEntity.status(HttpStatus.OK).body(packService.getPackInfoByPackId(packId));

    }

}
