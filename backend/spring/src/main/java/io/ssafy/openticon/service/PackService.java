package io.ssafy.openticon.service;


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
import lombok.extern.slf4j.Slf4j;
import io.ssafy.openticon.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.madgag.gif.fmsware.GifDecoder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;
import javax.security.sasl.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
public class PackService {

    private final ImageHashService imageHashService;
    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final DownloadRepository downloadRepository;
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


    public PackService(WebClient webClient, PackRepository packRepository, MemberService memberService, EmoticonService emoticonService, PermissionService permissionService, TagRepository tagRepository, TagListRepository tagListRepository, PurchaseHistoryService purchaseHistoryService, SafeSearchService safeSearchService, ImageHashService imageHashService, ObjectionService objectionService, RedisViewService redisViewService, PurchaseHistoryRepository purchaseHistoryRepository, DownloadRepository downloadRepository){
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
        this.purchaseHistoryRepository = purchaseHistoryRepository;
        this.downloadRepository = downloadRepository;
    }

    @Transactional
    public EmoticonPackResponseDto emoticonPackUpload(EmoticonPack emoticonPack) {

        AtomicBoolean detectPass = null;

        if(isDuplicateTitle(emoticonPack.getPackTitle())){
            throw new OpenticonException(ErrorCode.DUPLICATE_PACK_TITLE);
        }
        MemberEntity member = memberService.getMemberByEmail(emoticonPack.getUsername()).orElseThrow();
        CompletableFuture<Void> detectFuture = CompletableFuture.runAsync(() -> detect(emoticonPack, detectPass));


        List<MultipartFile> emoticonList = emoticonPack.getEmoticons();
        MultipartFile thumbnailImg = emoticonPack.getThumbnailImg();
        MultipartFile listImg = emoticonPack.getListImg();


        EmoticonFileAndName thumbnamilDto = new EmoticonFileAndName();
        EmoticonFileAndName listImgDto = new EmoticonFileAndName();


        CompletableFuture<Void> thumbnailSaveFuture = CompletableFuture.runAsync(() -> saveImage(thumbnailImg, thumbnamilDto));

        CompletableFuture<Void> listImgSaveFuture = CompletableFuture.runAsync(() -> saveImage(listImg, listImgDto));


        CompletableFuture<EmoticonPackEntity> allSaveFutures = CompletableFuture.allOf(thumbnailSaveFuture, listImgSaveFuture)
                .thenApply(v -> {
                    try {
                        // EmoticonPackEntity 생성 및 저장
                        EmoticonPackEntity emoticonPackEntity = new EmoticonPackEntity(emoticonPack, member, thumbnamilDto.getUrl(), listImgDto.getUrl());
                        packRepository.save(emoticonPackEntity);
                        DownloadEntity download = DownloadEntity.builder()
                                .emoticonPack(emoticonPackEntity)
                                .count(0)
                                .build();
                        downloadRepository.save(download);

                        // 해시 저장
                        imageHashService.saveThumbnailHash(thumbnamilDto.getFile(), emoticonPackEntity);
                        imageHashService.saveListImgHash(listImgDto.getFile(), emoticonPackEntity);

                        return emoticonPackEntity;  // 이후 작업에서 사용할 수 있도록 반환
                    } catch (IOException e) {
                        throw new RuntimeException("hash 실패", e);
                    }
                });

        List<String> emoticonsUrls = new ArrayList<>();


        // EmoticonPackEntity 생성 후 진행할 emoticons 비동기 저장 및 해시 작업
        CompletableFuture<Void> allFutures = allSaveFutures.thenCompose(emoticonPackEntity -> {
            List<CompletableFuture<Void>> futures = new ArrayList<>();


            for (int i=0;i<emoticonList.size();i++) {
                EmoticonFileAndName emoticonDto = new EmoticonFileAndName();

                // 각 이미지 저장 및 해시 생성 작업을 비동기로 실행
                int finalI = i;
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        // 이미지 저장
                        saveImage(emoticonList.get(finalI), emoticonDto);
                        //emoticonsUrls.add(emoticonDto.getUrl());
                        emoticonService.saveEmoticon(emoticonDto.getUrl(),emoticonPackEntity,finalI);
                        // 이미지 해시 저장
                        imageHashService.saveEmoticonHash(emoticonDto.getFile(), emoticonPackEntity, finalI);
                    } catch (IOException e) {
                        throw new RuntimeException("emoticonHash 실패: " + emoticonDto.getFile(), e);
                    }
                });

                futures.add(future);

            }

            // 모든 futures 작업이 완료될 때까지 대기
            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        });

        allFutures.join();

        EmoticonPackEntity emoticonPackEntity = allSaveFutures.join();

        if (emoticonPackEntity.getBlacklist()) {
            objectionService.objectionEmoticonPack(emoticonPackEntity, ReportType.EXAMINE);
        }

//        emoticonService.saveEmoticons(emoticonsUrls, emoticonPackEntity);

        saveTag(emoticonPackEntity,emoticonPack);

        detectFuture.orTimeout(10, TimeUnit.SECONDS)
                .exceptionally(ex -> {
                    // 타임아웃이나 예외가 발생했을 때 처리
                    emoticonPackEntity.setBlacklist(true);
                    packRepository.save(emoticonPackEntity);
                    throw new OpenticonException(ErrorCode.TIMEOUT);
                });
        return new EmoticonPackResponseDto(emoticonPackEntity);

    }

    private void saveTag(EmoticonPackEntity emoticonPackEntity, EmoticonPack emoticonPack){
        List<String> tagNames = emoticonPack.getTags();
        List<TagListEntity> tagListEntities = new ArrayList<>();
        for (String tag : tagNames) {
            TagEntity findTagEntity;
            Optional<TagEntity> getTagEntity = tagRepository.findByTagName(tag);
            if (getTagEntity.isEmpty()) {
                TagEntity tagEntity = TagEntity.builder()
                        .tagName(tag)
                        .build();
                tagRepository.save(tagEntity);
                findTagEntity = tagEntity;
            } else {
                findTagEntity = getTagEntity.get();
            }
            TagListEntity tagListEntity = TagListEntity.builder()
                    .emoticonPack(emoticonPackEntity)
                    .tag(findTagEntity)
                    .build();
            tagListRepository.save(tagListEntity);
            tagListEntities.add(tagListEntity);
        }
        emoticonPackEntity.setTagLists(tagListEntities);
    }

    private void detect(EmoticonPack emoticonPack, AtomicBoolean detectPass) {
        List<MultipartFile> images = new ArrayList<>();
        images.add(emoticonPack.getThumbnailImg());
        images.add(emoticonPack.getListImg());
        images.addAll(emoticonPack.getEmoticons());

        for (int i = 0; i < images.size(); i += 16) {
            int end = Math.min(i + 16, images.size());
            List<MultipartFile> subList = images.subList(i, end);

            CompletableFuture.runAsync(() -> {
                try {
                    if (safeSearchService.detectSafeSearch(subList)) {
                        detectPass.set(false);  // 유해 이미지 발견 시 detectPass를 false로 설정
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private boolean isDuplicateTitle(String title){
        if(packRepository.findByTitle(title).isPresent()){
            return true;
        }
        return false;
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

        if(emoticonPackEntity.getBlacklist()){
            throw new OpenticonException(ErrorCode.BLACKLIST_PACK);
        }

        List<String> emoticons=emoticonService.getEmoticons(emoticonPackEntity.getId());

        redisViewService.incrementView(emoticonPackEntity, userDetails, requestIp);
        emoticonPackEntity.setView(redisViewService.getRedisView(emoticonPackEntity));
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
        emoticonPackEntity.setView(redisViewService.getRedisView(emoticonPackEntity));
        return new PackInfoResponseDto(emoticonPackEntity,emoticons);
    }

    public Page<EmoticonPackResponseDto> search(String query, String type, Pageable pageable) {
        if (query == null || query.isEmpty()) {
            return convertToDtoPage(packRepository.findAllByIsPublicTrueAndIsBlacklistFalse(pageable));
        }

        Page<EmoticonPackEntity> entities;
        switch (type.toLowerCase()) {
            case "title":
                entities = packRepository.findByTitleContaining(query, pageable);
                break;
            case "tag":
                entities = packRepository.findByTag(query, pageable);
                break;
            case "author":
                entities = packRepository.findByAuthorContaining(query, pageable);
                break;
            default:
                entities = packRepository.findAll(pageable);
                break;
        }
        return convertToDtoPage(entities);
    }

    private Page<EmoticonPackResponseDto> convertToDtoPage(Page<EmoticonPackEntity> entities) {
        return entities.map(emoticonPackEntity -> {
            EmoticonPackResponseDto dto = new EmoticonPackResponseDto(emoticonPackEntity);

            // Redis에서 조회수 가져오기
            Long redisView = redisViewService.getRedisView(emoticonPackEntity);
            if (redisView != -1L) {
                dto.setView(redisView);
            }

            return dto;
        });
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

    @Transactional
    public void downloadInit() {
        // Step 1: 모든 이모티콘 팩에 대해 download 테이블에 레코드를 생성
        List<EmoticonPackEntity> emoticonPacks = packRepository.findAll();

        for (EmoticonPackEntity emoticonPack : emoticonPacks) {
            // 각 이모티콘 팩에 대한 DownloadEntity를 확인하여 없으면 새로 생성
            DownloadEntity download = downloadRepository.findByEmoticonPackId(emoticonPack.getId());

            if (download == null) {
                // DownloadEntity가 없으면 새로 생성
                download = new DownloadEntity();
                download.setEmoticonPack(emoticonPack);
                download.setCount(0);  // 기본 다운로드 수는 0으로 설정
                downloadRepository.save(download);  // 새로 생성된 DownloadEntity 저장
            }
        }

        // Step 2: purchaseHistory 데이터를 기반으로 다운로드 수 업데이트
        List<Object[]> downloadCounts = purchaseHistoryRepository.findDownloadCountsByEmoticonPack();

        for (Object[] row : downloadCounts) {
            Long emoticonPackId = (Long) row[0];
            Long downloadCount = (Long) row[1];

            // 이미 존재하는 DownloadEntity를 조회하고 다운로드 수 업데이트
            DownloadEntity download = downloadRepository.findByEmoticonPackId(emoticonPackId);

            if (download != null) {
                download.setCount(downloadCount.intValue());
                downloadRepository.save(download);  // 업데이트된 DownloadEntity 저장
            }
        }
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
