package io.ssafy.openticon.service;

import io.ssafy.openticon.controller.response.PackInfoResponseDto;
import io.ssafy.openticon.dto.EmoticonPack;
import io.ssafy.openticon.dto.ImageUrl;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.repository.PackRepository;
import jakarta.transaction.Transactional;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;


import javax.security.sasl.AuthenticationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PackService {

    private final WebClient webClient;
    private final PackRepository packRepository;
    private final MemberService memberService;
    private final EmoticonService emoticonService;
    private final PermissionService permissionService;

    public PackService(WebClient webClient, PackRepository packRepository, MemberService memberService, EmoticonService emoticonService, PermissionService permissionService){
        this.webClient=webClient;
        this.packRepository=packRepository;
        this.memberService = memberService;
        this.emoticonService=emoticonService;
        this.permissionService = permissionService;
    }

    @Transactional
    public String emoticonPackUpload(EmoticonPack emoticonPack){
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
        emoticonService.saveEmoticons(emoticonsUrls,emoticonPackEntity);

        return emoticonPackEntity.getShareLink();
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

    public PackInfoResponseDto getPackInfo(String uuid, String email) throws AuthenticationException {

        EmoticonPackEntity emoticonPackEntity=packRepository.findByShareLink(uuid);

        if(!validatePrivatePack(email,emoticonPackEntity.getId())){
            throw new AuthenticationException("접근 권한이 없습니다.");
        }

        List<String> emoticons=emoticonService.getEmoticons(emoticonPackEntity.getId());

        return new PackInfoResponseDto(emoticonPackEntity,emoticons);
    }

    private boolean validatePrivatePack(String email, Long packId){

        if(memberService.getMemberByEmail(email).isEmpty()){
            throw new IllegalArgumentException();
        }
        Long memberId=memberService.getMemberByEmail(email).get().getId();
        List<Long> accessedUsers=permissionService.permissionUsers(packId);

        for(Long user: accessedUsers){
            if(memberId.equals(user)){
                return true;
            }
        }
        return false;
    }
}
