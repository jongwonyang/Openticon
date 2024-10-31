package io.ssafy.openticon.service;

import io.ssafy.openticon.dto.ImageUrl;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class MemberService {

    private final WebClient webClient;
    private final MemberRepository memberRepository;

    @Value("${spring.image-server-url}")
    private String imageServerUrl;

    private MultiValueMap<String, Object> createMultipartBody(File file, String fileName) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("upload", new FileSystemResource(file));
        return body;
    }

    public MemberService(WebClient webClient, MemberRepository memberRepository) {
        this.webClient = webClient;
        this.memberRepository = memberRepository;
    }

    public Optional<MemberEntity> getMemberByEmail(String email){
        return memberRepository.findMemberByEmail(email);
    }

    public String saveProfile(MultipartFile image){
        String uploadServerUrl = imageServerUrl+ "/upload/profile";
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
}
