package io.ssafy.openticon.service;

import dev.brachtendorf.jimagehash.hash.Hash;
import dev.brachtendorf.jimagehash.hashAlgorithms.HashingAlgorithm;
import dev.brachtendorf.jimagehash.hashAlgorithms.PerceptiveHash;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.ImageHashEntity;
import io.ssafy.openticon.exception.ErrorCode;
import io.ssafy.openticon.exception.OpenticonException;
import io.ssafy.openticon.repository.ImageHashRepository;
import io.ssafy.openticon.repository.PackRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

@Service
public class ImageHashService {


    private final PackRepository packRepository;
    private final ImageHashRepository imageHashRepository;

    public ImageHashService(PackRepository packRepository, ImageHashRepository imageHashRepository) {
        this.packRepository = packRepository;
        this.imageHashRepository = imageHashRepository;
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
}
