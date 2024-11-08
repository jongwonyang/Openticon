package io.ssafy.openticon.service;

import dev.brachtendorf.jimagehash.hash.Hash;
import dev.brachtendorf.jimagehash.hashAlgorithms.HashingAlgorithm;
import dev.brachtendorf.jimagehash.hashAlgorithms.PerceptiveHash;
import io.ssafy.openticon.controller.response.ImageHashResponseDto;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.ImageHashEntity;
import io.ssafy.openticon.exception.ErrorCode;
import io.ssafy.openticon.exception.OpenticonException;
import io.ssafy.openticon.repository.ImageHashRepository;
import io.ssafy.openticon.repository.PackRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ImageHashService {


    private final PackRepository packRepository;
    private final ImageHashRepository imageHashRepository;
    private final EmoticonService emoticonService;

    public ImageHashService(PackRepository packRepository, ImageHashRepository imageHashRepository, EmoticonService emoticonService) {
        this.packRepository = packRepository;
        this.imageHashRepository = imageHashRepository;
        this.emoticonService = emoticonService;
    }

    public void saveThumbnailHash(File thumbnail, EmoticonPackEntity emoticonPackEntity) throws IOException {
        HashingAlgorithm hasher = new PerceptiveHash(32);
        Hash thumbnailHash=hasher.hash(thumbnail);

        ImageHashEntity imageHashEntity=ImageHashEntity.builder().
                emoticonPack(emoticonPackEntity)
                .isThumbnail(true)
                .hash(thumbnailHash)
                .build();

        imageHashRepository.save(imageHashEntity);
    }

    public void saveListImgHash(File listImg, EmoticonPackEntity emoticonPackEntity) throws IOException {
        HashingAlgorithm hasher = new PerceptiveHash(32);
        Hash listImgHash=hasher.hash(listImg);

        ImageHashEntity imageHashEntity=ImageHashEntity.builder().
                emoticonPack(emoticonPackEntity)
                .isListImg(true)
                .hash(listImgHash)
                .build();

        imageHashRepository.save(imageHashEntity);
    }

    @Transactional
    public void saveEmoticonHash(File emoticon, EmoticonPackEntity emoticonPackEntity, int order) throws IOException {
        HashingAlgorithm hasher = new PerceptiveHash(32);
        Hash emoticonHash=hasher.hash(emoticon);

        ImageHashEntity imageHashEntity=ImageHashEntity.builder().
                emoticonPack(emoticonPackEntity)
                .emoticonOrder(order)
                .hash(emoticonHash)
                .build();

        imageHashRepository.save(imageHashEntity);
    }

    public void checkDuplicate(File userThumbnail, File userListImg) throws IOException {
        HashingAlgorithm hasher=new PerceptiveHash(32);

        Hash hashUserThumbnail=hasher.hash(userThumbnail);
        Hash hashUserListImg=hasher.hash(userListImg);

        List<EmoticonPackEntity> emoticonPackEntityList=packRepository.findAll();

        for(EmoticonPackEntity emoticonPack: emoticonPackEntityList){
            Hash hashDbThumbnail=hasher.hash(new File(emoticonPack.getThumbnailImg()));
            Hash hashDbListImg=hasher.hash(new File(emoticonPack.getListImg()));

            double similarityThumbnail=hashDbThumbnail.normalizedHammingDistance(hashUserThumbnail);
            double similarityListImg=hashDbListImg.normalizedHammingDistance(hashUserListImg);

            if(similarityThumbnail<.2){
                throw new OpenticonException(ErrorCode.DUPLICATE_THUMBNAIL);
            }
            if(similarityListImg<.2){
                throw new OpenticonException(ErrorCode.DUPLICATE_LIST_IMG);
            }


        }
    }

    private File makeFile(MultipartFile image){
        File tempFile = null;
        try {
            tempFile = File.createTempFile("find", image.getOriginalFilename());
            image.transferTo(tempFile);
            return tempFile;
        }catch (IOException e){
            throw new RuntimeException("File만들기 실패");
        }

    }

    public Page<ImageHashResponseDto> searchImage(MultipartFile findImage, Pageable pageable) throws IOException {
        File image=makeFile(findImage);
        HashingAlgorithm hasher = new PerceptiveHash(32);
        Hash imageHash=hasher.hash(image);
        List<ImageHashEntity> imageHashEntities=imageHashRepository.findAll(pageable.getSort());
        Set<Long> alreadyFound=new HashSet<>();
        List<ImageHashResponseDto> result=new ArrayList<>();
        for(ImageHashEntity imageHashEntity: imageHashEntities){
            Hash serverImageHash=new Hash(imageHashEntity.getHashValue(),imageHashEntity.getHashLength(),imageHashEntity.getAlgorithmId());
            double similarity=serverImageHash.normalizedHammingDistance(imageHash);
            if(similarity<.1){
//                String target;
//                if(imageHashEntity.getIsThumbnail()){
//                    target=imageHashEntity.getEmoticonPackEntity().getThumbnailImg();
//                }else if(imageHashEntity.getIsListImg()){
//                    target=imageHashEntity.getEmoticonPackEntity().getListImg();
//                }else{
//                    target=emoticonService.getEmoticon(imageHashEntity.getEmoticonPackEntity(),imageHashEntity.getEmoticonOrder());
//                }
                if(alreadyFound.contains(imageHashEntity.getEmoticonPackEntity().getId()))continue;
                alreadyFound.add(imageHashEntity.getEmoticonPackEntity().getId());
                result.add(new ImageHashResponseDto(imageHashEntity.getEmoticonPackEntity()));
            }
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), result.size());
        List<ImageHashResponseDto> paginatedResult = result.subList(start, end);

        // `PageImpl`을 사용하여 페이지네이션된 결과를 생성합니다.
        return new PageImpl<>(paginatedResult, pageable, result.size());
    }
}
