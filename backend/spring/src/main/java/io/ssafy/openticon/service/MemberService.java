package io.ssafy.openticon.service;

import io.ssafy.openticon.dto.ImageUrl;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class MemberService {

    private final WebClient webClient;
    private final MemberRepository memberRepository;

    @Value("${spring.image-server-url}")
    private String imageServerUrl;

    public MemberService(WebClient webClient, MemberRepository memberRepository) {
        this.webClient = webClient;
        this.memberRepository = memberRepository;
    }

    private MultiValueMap<String, Object> createMultipartBody(File file) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("upload", new FileSystemResource(file));
        return body;
    }

    public Optional<MemberEntity> getMemberByEmail(String email) {
        return memberRepository.findMemberByEmail(email);
    }

    public String saveProfile(MultipartFile image, boolean applyWhiteBackground) {
        String uploadServerUrl = imageServerUrl + "/upload/image";
        File tempFile = null;
        try {
            // 원본 이미지 BufferedImage로 읽기
            BufferedImage originalImage = ImageIO.read(image.getInputStream());

            // 배경색 변경 조건: 옵션이 true이고 파일 확장자가 png일 때만 적용
            BufferedImage processedImage = originalImage;
            if (applyWhiteBackground && "image/png".equals(image.getContentType())) {
                processedImage = convertTransparentToWhiteBackground(originalImage);
            }

            // 처리된 이미지를 임시 파일로 저장
            tempFile = File.createTempFile("upload", ".png");
            ImageIO.write(processedImage, "png", tempFile);

            // WebClient를 사용하여 임시 파일을 서버에 업로드
            ImageUrl imageUrl = webClient.post()
                    .uri(uploadServerUrl)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .bodyValue(createMultipartBody(tempFile))
                    .retrieve()
                    .bodyToMono(ImageUrl.class)
                    .block();

            return imageUrl.getUrl();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete(); // 임시 파일 삭제
            }
        }
    }

    // 배경을 흰색으로 설정하는 메서드
    private BufferedImage convertTransparentToWhiteBackground(BufferedImage originalImage) {
        BufferedImage newImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        Graphics2D g2d = newImage.createGraphics();
        g2d.setPaint(Color.WHITE); // 흰색 배경 설정
        g2d.fillRect(0, 0, newImage.getWidth(), newImage.getHeight());
        g2d.drawImage(originalImage, 0, 0, null);
        g2d.dispose();

        return newImage;
    }
}
