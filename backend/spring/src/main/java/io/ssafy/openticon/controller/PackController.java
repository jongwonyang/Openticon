package io.ssafy.openticon.controller;

import io.ssafy.openticon.controller.request.EmoticonUploadRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class PackController {

    public ResponseEntity<Void> uploadEmoticon(@AuthenticationPrincipal UserDetails userDetails,
                                               @RequestPart("packInfo") EmoticonUploadRequestDto emoticonUploadRequest,
                                               @RequestPart("thumbnail")MultipartFile thumbnail,
                                               @RequestPart("thumbnail_list") MultipartFile thumbnailList,
                                               @RequestPart("emoticons")List<MultipartFile> emoticons
                                               ){
        return ResponseEntity.noContent().build();
    }
}
