package io.ssafy.openticon.service;

import dev.brachtendorf.jimagehash.hashAlgorithms.HashingAlgorithm;
import dev.brachtendorf.jimagehash.hashAlgorithms.PerceptiveHash;
import io.ssafy.openticon.controller.request.ReportPackRequestDto;
import io.ssafy.openticon.controller.response.EmoticonPackResponseDto;
import io.ssafy.openticon.controller.response.PackDownloadResponseDto;
import io.ssafy.openticon.controller.response.PackInfoResponseDto;
import io.ssafy.openticon.dto.*;
import io.ssafy.openticon.entity.*;
import io.ssafy.openticon.exception.ErrorCode;
import io.ssafy.openticon.exception.OpenticonException;
import io.ssafy.openticon.repository.PackRepository;
import io.ssafy.openticon.repository.TagListRepository;
import io.ssafy.openticon.repository.TagRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.madgag.gif.fmsware.GifDecoder;
import com.madgag.gif.fmsware.AnimatedGifEncoder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;
import javax.security.sasl.AuthenticationException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class PackService {

    private final ImageHashService imageHashService;
    @Value("${spring.image-server-url}")
    private String imageServerUrl;

    private final WebClient webClient;
    private final PackRepository packRepository;
    private final MemberService memberService;
    private final EmoticonService emoticonService;
    private final PermissionService permissionService;
    private final TagRepository tagRepository;
    private final TagListRepository tagListRepository;
    private final PurchaseHistoryService purchaseHistoryService;
    private final SafeSearchService safeSearchService;
    private final ObjectionService objectionService;
    private final RedisViewService redisViewService;


    public PackService(WebClient webClient, PackRepository packRepository, MemberService memberService, EmoticonService emoticonService, PermissionService permissionService, TagRepository tagRepository, TagListRepository tagListRepository, PurchaseHistoryService purchaseHistoryService, SafeSearchService safeSearchService, ImageHashService imageHashService, ObjectionService objectionService, RedisViewService redisViewService){
        this.webClient=webClient;
        this.packRepository=packRepository;
        this.memberService = memberService;
        this.emoticonService=emoticonService;
        this.permissionService = permissionService;
        this.tagRepository = tagRepository;
        this.tagListRepository = tagListRepository;
        this.purchaseHistoryService = purchaseHistoryService;
        this.safeSearchService = safeSearchService;
        this.imageHashService = imageHashService;
        this.objectionService = objectionService;
        this.redisViewService = redisViewService;
    }

    @Transactional
    public EmoticonPackResponseDto emoticonPackUpload(EmoticonPack emoticonPack) {
        try {
            System.out.println("Step 1: Start processing EmoticonPack upload.");

            List<MultipartFile> infoImages = new ArrayList<>();
            infoImages.add(emoticonPack.getThumbnailImg());
            infoImages.add(emoticonPack.getListImg());

            System.out.println("Step 2: Checking for problematic images.");
            boolean problematicInfoImage = safeSearchService.detectSafeSearch(infoImages); // true면 이상한 이미지
            System.out.println("Step 2 Result: Problematic info image found? " + problematicInfoImage);

            List<MultipartFile> emoticonList = emoticonPack.getEmoticons();
            MultipartFile thumbnailImg = emoticonPack.getThumbnailImg();
            MultipartFile listImg = emoticonPack.getListImg();
            System.out.println("Step 3: Retrieved thumbnail and list images.");

            EmoticonFileAndName thumbnamilDto = new EmoticonFileAndName();
            EmoticonFileAndName listImgDto = new EmoticonFileAndName();

            System.out.println("Step 4: Saving thumbnail and list images.");
            saveImage(thumbnailImg, thumbnamilDto);
            saveImage(listImg, listImgDto);
//            System.out.println("Step 4 Result: Thumbnail URL - " + thumbnailImgUrl + ", List Image URL - " + listImgUrl);

            List<String> emoticonsUrls = new ArrayList<>();
            System.out.println("Step 5: Fetching member by username - " + emoticonPack.getUsername());
            MemberEntity member = memberService.getMemberByEmail(emoticonPack.getUsername()).orElseThrow();
            EmoticonPackEntity emoticonPackEntity = new EmoticonPackEntity(emoticonPack, member, thumbnamilDto.getUrl(), listImgDto.getUrl());
            System.out.println("Step 6: Created EmoticonPackEntity.");

            boolean problematicImage = false;
            System.out.println("Step 7: Checking emoticon images for safety.");
            for (int i = 0; i < emoticonList.size(); i += 16) {
                int end = Math.min(i + 16, emoticonList.size());
                List<MultipartFile> subList = emoticonList.subList(i, end);

                if (safeSearchService.detectSafeSearch(subList)) {
                    problematicImage = true;
                    System.out.println("Step 7 Warning: Problematic emoticon image found in sublist.");
                }
            }

            if (problematicImage || problematicInfoImage) {
                emoticonPackEntity.setBlacklist(true);
                System.out.println("Step 8: Emoticon pack marked as blacklisted.");
            }

            packRepository.save(emoticonPackEntity);
            System.out.println("Step 9: Emoticon pack saved.");

            if (problematicImage || problematicInfoImage) {
                objectionService.objectionEmoticonPack(emoticonPackEntity, ReportType.EXAMINE);
                System.out.println("Step 10: Objection registered for EmoticonPackEntity.");
            }

            imageHashService.saveThumbnailHash(thumbnamilDto.getFile(), emoticonPackEntity);
            imageHashService.saveListImgHash(listImgDto.getFile(), emoticonPackEntity);
            System.out.println("Step 11: Saved image hashes for thumbnail and list image.");

            int cnt = 0;
            System.out.println("Step 12: Saving emoticon images.");
            for (MultipartFile emoticon : emoticonList) {
                EmoticonFileAndName emoticonDto = new EmoticonFileAndName();
//                File emoticonFile = makeFile(emoticon);
                saveImage(emoticon, emoticonDto);
                emoticonsUrls.add(emoticonDto.getUrl());
                imageHashService.saveEmoticonHash(emoticonDto.getFile(), emoticonPackEntity, cnt);
                cnt++;
                System.out.println("Step 12: Emoticon image " + cnt + " processed and saved.");
            }
            emoticonService.saveEmoticons(emoticonsUrls, emoticonPackEntity);
            System.out.println("Step 13: Emoticons saved.");
            // 태그 정보 추가
            List<String> tagNames = emoticonPack.getTags();
            List<TagListEntity> tagListEntities = new ArrayList<>();
            System.out.println("Step 14: Processing tags.");
            for (String tag : tagNames) {
                TagEntity findTagEntity;
                Optional<TagEntity> getTagEntity = tagRepository.findByTagName(tag);
                if (getTagEntity.isEmpty()) {
                    TagEntity tagEntity = TagEntity.builder()
                            .tagName(tag)
                            .build();
                    tagRepository.save(tagEntity);
                    findTagEntity = tagEntity;
                    System.out.println("Step 14: New tag created - " + tag);
                } else {
                    findTagEntity = getTagEntity.get();
                    System.out.println("Step 14: Existing tag found - " + tag);
                }
                TagListEntity tagListEntity = TagListEntity.builder()
                        .emoticonPack(emoticonPackEntity)
                        .tag(findTagEntity)
                        .build();
                tagListRepository.save(tagListEntity);
                tagListEntities.add(tagListEntity);
            }
            emoticonPackEntity.setTagLists(tagListEntities);
            System.out.println("Step 15: Tags processed and saved.");

            return new EmoticonPackResponseDto(emoticonPackEntity);
        } catch (IOException e) {
            System.err.println("Exception during EmoticonPack upload: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    private File makeFile(MultipartFile image){
        File tempFile = null;
        try {
            tempFile = File.createTempFile("upload", image.getOriginalFilename());
            image.transferTo(tempFile);
            return tempFile;
        }catch (IOException e){
            throw new RuntimeException("File만들기 실패");
        }

    }




    private void saveImage(MultipartFile image, EmoticonFileAndName dto) {
        String uploadServerUrl = imageServerUrl + "/upload/image";

        try {
            // 원본 이미지가 GIF인 경우
            if ("image/gif".equals(image.getContentType())) {
                GifDecoder gifDecoder = new GifDecoder();
                gifDecoder.read(image.getInputStream());

                AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
                dto.setFile(File.createTempFile("upload", ".gif"));
                gifEncoder.start(dto.getFile().getPath());
                gifEncoder.setRepeat(0);

                for (int i = 0; i < gifDecoder.getFrameCount(); i++) {
                    BufferedImage frame = gifDecoder.getFrame(i);
                    BufferedImage processedFrame = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2d = processedFrame.createGraphics();
                    g2d.setColor(Color.WHITE); // 배경 흰색으로 채우기
                    g2d.fillRect(0, 0, frame.getWidth(), frame.getHeight());
                    g2d.drawImage(frame, 0, 0, null);
                    g2d.dispose();
                    gifEncoder.addFrame(processedFrame);
                    gifEncoder.setDelay(gifDecoder.getDelay(i));
                }
                gifEncoder.finish();

            } else {
                // PNG 및 기타 포맷의 경우
                BufferedImage originalImage = ImageIO.read(image.getInputStream());
                BufferedImage processedImage = originalImage;

                if ("image/png".equals(image.getContentType())) {
                    processedImage = convertTransparentToWhiteBackground(originalImage);
                }

                dto.setFile(File.createTempFile("upload", ".png"));
                ImageIO.write(processedImage, "png", dto.getFile());
            }

            ImageUrl imageUrl = webClient.post()
                    .uri(uploadServerUrl)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .bodyValue(createMultipartBody(dto.getFile(), image.getOriginalFilename()))
                    .retrieve()
                    .bodyToMono(ImageUrl.class)
                    .block();

            dto.setUrl(imageUrl.getUrl());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }



    private MultiValueMap<String, Object> createMultipartBody(File file, String fileName) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("upload", new FileSystemResource(file));
        return body;
    }

    public PackInfoResponseDto getPackInfo(String uuid, UserDetails userDetails, String requestIp) throws AuthenticationException {

        EmoticonPackEntity emoticonPackEntity=packRepository.findByShareLink(uuid);

//        if(!validatePrivatePack(email,emoticonPackEntity.getId()) && !emoticonPackEntity.isPublic()){
//            throw new OpenticonException(ErrorCode.PRIVATE_PACK);
//        }

        if(emoticonPackEntity.getBlacklist()){
            throw new OpenticonException(ErrorCode.BLACKLIST_PACK);
        }

        List<String> emoticons=emoticonService.getEmoticons(emoticonPackEntity.getId());

        redisViewService.incrementView(emoticonPackEntity, userDetails, requestIp);
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

    public PackInfoResponseDto getPackInfoByPackId(String emoticonPackId, UserDetails userDetails, String requestIp){

        Long packId=Long.parseLong(emoticonPackId);
        if(packRepository.findById(packId).isEmpty()){
            throw new IllegalArgumentException("해당하는 EmoticonPack 이 없음");
        }

        EmoticonPackEntity emoticonPackEntity=packRepository.findById(packId).get();

        if(!emoticonPackEntity.isPublic()){
            throw new OpenticonException(ErrorCode.PRIVATE_PACK);
        }

        if(emoticonPackEntity.getBlacklist()){
            throw new OpenticonException(ErrorCode.BLACKLIST_PACK);
        }

        List<String> emoticons=emoticonService.getEmoticons(emoticonPackEntity.getId());
        redisViewService.incrementView(emoticonPackEntity, userDetails, requestIp);
        return new PackInfoResponseDto(emoticonPackEntity,emoticons);
    }

    public Page<EmoticonPackResponseDto> search(String query, String type, Pageable pageable) {
        if (query == null || query.isEmpty()) {
            return packRepository.findAllByIsPublicTrueAndIsBlacklistFalse(pageable).map(EmoticonPackResponseDto::new); // 검색어가 없으면 전체 조회
        }

        // TODO: type 기준
        switch (type.toLowerCase()) {
            case "title":
                return packRepository.findByTitleContaining(query, pageable).map(EmoticonPackResponseDto::new); // 부분 일치
            case "tag":
                return packRepository.findByTag(query, pageable).map(EmoticonPackResponseDto::new);   // 부분 일치
            case "author":
                return packRepository.findByAuthorContaining(query, pageable).map(EmoticonPackResponseDto::new); // 부분 일치
            default:
                return packRepository.findAll(pageable).map(EmoticonPackResponseDto::new); // 기본 전체 조회
        }
    }

    public PackDownloadResponseDto downloadPack(String email, Long packId) {
        MemberEntity member=memberService.getMemberByEmail(email).orElseThrow();
        EmoticonPackEntity emoticonPackEntity=packRepository.findById(packId).orElseThrow();
        if(!purchaseHistoryService.isMemberPurchasePack(member,emoticonPackEntity)){
            throw new OpenticonException(ErrorCode.ACCESS_DENIED);
        }

        String thumbnailImg=emoticonPackEntity.getThumbnailImg();
        String listImg=emoticonPackEntity.getListImg();
        List<String> emoticons=emoticonService.getEmoticons(packId);
        return new PackDownloadResponseDto(thumbnailImg,listImg,emoticons);
    }

    public Optional<EmoticonPackEntity> getPackById(Long packId){
        return packRepository.findById(packId);
    }

    @Transactional
    public void save(EmoticonPackEntity emoticonPackEntity){
        packRepository.save(emoticonPackEntity);
    }

    @Transactional
    public Page<EmoticonPackResponseDto> myPackList(MemberEntity member, Pageable pageable){
        return packRepository.findByMyEmoticonPack(member, pageable).map(EmoticonPackResponseDto::new);

    }
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
