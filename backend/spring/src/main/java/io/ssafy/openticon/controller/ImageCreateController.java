package io.ssafy.openticon.controller;

import io.ssafy.openticon.controller.request.ImageCreateRequestDto;
import io.ssafy.openticon.dto.GpuRequest;
import jakarta.servlet.http.HttpServletResponse;
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

import java.io.InputStream;

@RestController
@RequestMapping("/ai")
public class ImageCreateController {

    private final WebClient webClient;


    public ImageCreateController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://222.107.238.124:7777").build();
    }

    @PostMapping("/create-image")
    public Mono<ResponseEntity<InputStreamResource>> createImage(@RequestBody ImageCreateRequestDto imageCreateRequestDto,
                                                                 HttpServletResponse response){

        String prompt=imageCreateRequestDto.getPrompt();
        GpuRequest gpuRequest=new GpuRequest(prompt, 20, 7);
        return webClient
                .post()
                .bodyValue(gpuRequest)  // 사용자가 보낸 문자열을 요청 본문에 넣음
                .retrieve()
                .bodyToMono(InputStream.class)  // GPU 서버로부터 PNG 파일을 InputStream으로 받음
                .map(pngStream -> {
                    // 응답 헤더 설정
                    response.setContentType(MediaType.IMAGE_PNG_VALUE);
                    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"generated-image.png\"");
                    System.out.println(pngStream);
                    // PNG 파일을 클라이언트에게 스트리밍으로 전송
                    return ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_PNG)
                            .body(new InputStreamResource(pngStream));  // InputStreamResource를 사용하여 파일 스트리밍
                });

    }
}
