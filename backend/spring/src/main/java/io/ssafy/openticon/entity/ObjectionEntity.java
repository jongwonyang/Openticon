package io.ssafy.openticon.entity;

import io.ssafy.openticon.dto.PointType;
import io.ssafy.openticon.dto.ReportStateType;
import io.ssafy.openticon.dto.ReportType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "objection")
public class ObjectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emoticon_pack_id", nullable = false)
    private EmoticonPackEntity emoticonPack;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @Builder.Default
    private ReportType type = ReportType.EXAMINE;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    @Builder.Default
    private ReportStateType state = ReportStateType.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private OffsetDateTime createdAt = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toOffsetDateTime();

    @Column(name = "completed_at", nullable = true)
    private OffsetDateTime completedAt;

}
