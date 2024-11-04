package io.ssafy.openticon.service;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class SafeSearchService {

    @Transactional
    public boolean detectSafeSearch(List<MultipartFile> multipartFiles) throws IOException{
        // ture는 검출된 것, false는 정상적인 것
        // 유해성 검출 여부
        boolean detectedContent = false;
        StringBuilder sb = new StringBuilder();

        List<byte[]> imageBytesList = new ArrayList<>();
        for(MultipartFile image : multipartFiles){
            System.out.println(image.getName()+": "+image.getContentType());
            imageBytesList.add(image.getBytes());
        }
        List<AnnotateImageRequest> requests = new ArrayList<>();

        for(byte[] imageBytes : imageBytesList){
            ByteString imgBytes = ByteString.copyFrom(imageBytes);

            // 이미지 요청 설정
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Feature.Type.SAFE_SEARCH_DETECTION).build();
            AnnotateImageRequest request =
                    AnnotateImageRequest
                            .newBuilder()
                            .addFeatures(feat)
                            .setImage(img)
                            .build();
            requests.add(request);
        }

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

//            int cnt = 1;
            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    throw new RuntimeException("세이프 서치 도중 에러가 발생했습니다. 이미지를 다시 확인해주세요.");
//                    sb.append(String.format("%d 번째 이미지 ERROR\n", cnt));
                }else{
                    SafeSearchAnnotation annotation = res.getSafeSearchAnnotation();

                    // 가능성이 매우 높은 콘텐츠가 포함된 경우
                    if(annotation.getAdult().equals(Likelihood.VERY_LIKELY) ||
                            annotation.getMedical().equals(Likelihood.VERY_LIKELY) ||
                            annotation.getViolence().equals(Likelihood.VERY_LIKELY) ||
                            annotation.getRacy().equals(Likelihood.VERY_LIKELY)
                    ){
                        detectedContent = true;
                    }
//                    sb.append(String.format(
//                            "%d 번째 이미지\n" +
//                                    "성인 콘텐츠(adult): %s\n" +
//                                    "의료 콘텐츠(medical): %s\n" +
//                                    "합성 가능성(spoofed): %s\n" +
//                                    "폭력 콘텐츠(violence): %s\n" +
//                                    "자극적 콘텐츠(racy): %s\n",
//                            cnt,
//                            formatLikelihood(annotation.getAdult()),
//                            formatLikelihood(annotation.getMedical()),
//                            formatLikelihood(annotation.getSpoof()),
//                            formatLikelihood(annotation.getViolence()),
//                            formatLikelihood(annotation.getRacy())
//                    ));
                }
//                cnt++;
            }
        }
        return detectedContent;
    }

    private static String formatLikelihood(Likelihood likelihood) {
        switch (likelihood) {
            case VERY_UNLIKELY:
                return "VERY_UNLIKELY(매우 낮음)";
            case UNLIKELY:
                return "UNLIKELY(낮음)";
            case POSSIBLE:
                return "POSSIBLE(있음)";
            case LIKELY:
                return "LIKELY(높음)";
            case VERY_LIKELY:
                return "VERY_LIKELY(매우 높음)";
            default:
                return "UNKNOWN(알 수 없음)";
        }
    }
}
