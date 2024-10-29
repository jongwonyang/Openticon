package io.ssafy.openticon.controller;

import io.ssafy.openticon.controller.request.EmoticonUploadRequestDto;
import io.ssafy.openticon.dto.EmoticonPack;
import io.ssafy.openticon.service.PackService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
public class PackController {

    private final PackService packService;

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadEmoticon(@AuthenticationPrincipal UserDetails userDetails,
                                               @RequestPart("packInfo") EmoticonUploadRequestDto emoticonUploadRequest,
                                               @RequestPart("thumbnail_img")MultipartFile thumbnailImg,
                                               @RequestPart("list_img") MultipartFile listImg,
                                               @RequestPart("emoticons")List<MultipartFile> emoticons
                                               ){

        EmoticonPack emoticonPack=new EmoticonPack(emoticonUploadRequest,userDetails.getUsername());
        emoticonPack.setImages(thumbnailImg, listImg, emoticons);

        packService.emoticonPackUpload(emoticonPack);
        return ResponseEntity.noContent().build();
    }
}
