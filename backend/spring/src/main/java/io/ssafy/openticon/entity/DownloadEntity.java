package io.ssafy.openticon.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "download")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DownloadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emoticon_pack_id", nullable = false, unique = true)
    private EmoticonPackEntity emoticonPack;

    @Column(name = "count", nullable = false)
    private int count = 0;
}
