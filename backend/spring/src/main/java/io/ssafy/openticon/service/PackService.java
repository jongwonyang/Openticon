package io.ssafy.openticon.service;

import io.ssafy.openticon.dto.EmoticonPack;
import io.ssafy.openticon.dto.ImageUrl;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.repository.PackRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;


import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PackService {

    private final WebClient webClient;
    private final PackRepository packRepository;
    private final MemberService memberService;

    public PackService(WebClient webClient, PackRepository packRepository, MemberService memberService){
        this.webClient=webClient;
        this.packRepository=packRepository;
        this.memberService = memberService;
    }

    public void emoticonPackUpload(EmoticonPack emoticonPack){
        MultipartFile thumbnailImg= emoticonPack.getThumbnailImg();
        MultipartFile listImg= emoticonPack.getListImg();
        String thumbnailImgUrl=saveImage(thumbnailImg);
        String listImgUrl=saveImage(listImg);

        List<String> emoticonsUrls=new ArrayList<>();
        for(MultipartFile emoticon: emoticonPack.getEmoticons()){
            emoticonsUrls.add(saveImage(emoticon));
        }
        MemberEntity member=memberService.getMemberByEmail(emoticonPack.getUsername()).get();
        EmoticonPackEntity emoticonPackEntity=new EmoticonPackEntity(emoticonPack,member, thumbnailImgUrl,listImgUrl);
        packRepository.save(emoticonPackEntity);
    }

    private String saveImage(MultipartFile image){
        String uploadServerUrl="http://localhost:8070/upload/image";

        File tempFile = null;
        try {
            tempFile = File.createTempFile("upload", image.getOriginalFilename());
            image.transferTo(tempFile);

            ImageUrl imageUrl = webClient.post()
                    .uri(uploadServerUrl)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .bodyValue(createMultipartBody(tempFile, image.getOriginalFilename()))
                    .retrieve()
                    .bodyToMono(ImageUrl.class)
                    .block();

            return imageUrl.getUrl();

        } catch (IOException e) {
            throw new RuntimeException("Failed to save image",e);
        }


    }

    private MultiValueMap<String, Object> createMultipartBody(File file, String fileName) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("upload", new FileSystemResource(file));
        return body;
    }
}
