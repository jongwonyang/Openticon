package io.ssafy.openticon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "emoticon")
@Getter
@NoArgsConstructor
public class EmoticonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // 지연 로딩 설정
    @JoinColumn(name = "emoticon_pack_id", nullable = false)
    private EmoticonPackEntity emoticonPack;

    @Column(name="emoticon_order", nullable = false)
    private int emoticonOrder;

    @Column(name="image_path", nullable = false, columnDefinition = "TEXT DEFAULT ''")
    private String imagePath;

    public EmoticonEntity(EmoticonPackEntity emoticonPackEntity, int emoticonOrder, String imagePath){
        this.emoticonPack=emoticonPackEntity;
        this.emoticonOrder=emoticonOrder;
        this.imagePath=imagePath;
    }

}
