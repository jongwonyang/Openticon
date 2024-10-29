package io.ssafy.openticon.controller;

import io.ssafy.openticon.controller.request.EmoticonUploadRequestDto;
import io.ssafy.openticon.dto.EmoticonPack;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class PackController {

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadEmoticon(@AuthenticationPrincipal UserDetails userDetails,
                                               @RequestPart("packInfo") EmoticonUploadRequestDto emoticonUploadRequest,
                                               @RequestPart("thumbnail_img")MultipartFile thumbnailImg,
                                               @RequestPart("list_img") MultipartFile listImg,
                                               @RequestPart("emoticons")List<MultipartFile> emoticons
                                               ){

        EmoticonPack emoticonPack=new EmoticonPack(emoticonUploadRequest);
        emoticonPack.setImages(thumbnailImg, listImg, emoticons);
        return ResponseEntity.noContent().build();
    }
}
