package io.ssafy.openticon.entity;

import dev.brachtendorf.jimagehash.hash.Hash;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@Entity
@Table(name = "image_hash")
@Getter
@RequiredArgsConstructor
public class ImageHashEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // 지연 로딩 설정
    @JoinColumn(name = "pack_id", nullable = false)
    private EmoticonPackEntity emoticonPackEntity;

    @Column(name = "is_thumbnail")
    private Boolean isThumbnail;

    @Column(name = "is_list_img ")
    private Boolean isListImg;

    @Column(name = "emoticon_order")
    private Integer emoticonOrder;

    @Column(name = "algorithm_id")
    private int algorithmId;

    @Column(name = "hash_value")
    private BigInteger hashValue;

    @Column(name = "hash_length")
    private int hashLength;

    @Builder
    public ImageHashEntity(EmoticonPackEntity emoticonPack, boolean isThumbnail, boolean isListImg, Integer emoticonOrder, Hash hash){
        this.emoticonPackEntity=emoticonPack;
        this.isThumbnail=isThumbnail;
        this.isListImg=isListImg;
        this.emoticonOrder=emoticonOrder;
        this.algorithmId=hash.getAlgorithmId();
        this.hashValue=hash.getHashValue();
        this.hashLength=hash.getBitResolution();
    }

}
