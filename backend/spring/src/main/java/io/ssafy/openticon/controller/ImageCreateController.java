package io.ssafy.openticon.controller;

import io.ssafy.openticon.controller.request.ImageCreateRequestDto;
import io.ssafy.openticon.dto.GpuRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.awt.image.DataBuffer;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@RestController
@RequestMapping("/ai")
public class ImageCreateController {

    @Value("${spring.gpu-url}")
    private String GPU_URL;

    private final WebClient webClient;

    public ImageCreateController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.
                codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)).
                baseUrl(GPU_URL).build();
    }

    @PostMapping("/create-image")
    @Operation(summary = "GPU 서버에 이미지 생성 요청")
    public Mono<ResponseEntity<InputStreamResource>> createImage(@RequestBody ImageCreateRequestDto imageCreateRequestDto) {
        String prompt = imageCreateRequestDto.getPrompt();
        int seed = imageCreateRequestDto.getSeed();
        GpuRequest gpuRequest = new GpuRequest(prompt, seed, 20, 7);

        return webClient
                .post()
                .bodyValue(gpuRequest)
                .retrieve()
                // 상태 코드에 따른 오류 처리
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), clientResponse -> {
                    // 응답 상태 코드 및 본문 로그
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(body -> {
                                System.out.println("Error Response Body: " + body);
                                return Mono.error(new RuntimeException("GPU server error: " + body));
                            });
                })
                .bodyToMono(byte[].class)  // PNG 파일을 byte[] 형태로 받음
                .map(imageBytes -> {
                    // byte[] 데이터를 InputStream으로 변환
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);

                    // PNG 파일을 클라이언트에게 스트리밍으로 전송
                    return ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_PNG)
                            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"generated-image.png\"")
                            .body(new InputStreamResource(byteArrayInputStream));  // InputStreamResource로 스트리밍 파일 반환
                })
                .onErrorResume(e -> {
                    // 오류 발생 시 로그 추가
                    e.printStackTrace();
                    return Mono.error(new RuntimeException("Error generating image from GPU server", e));
                });
    }
}

