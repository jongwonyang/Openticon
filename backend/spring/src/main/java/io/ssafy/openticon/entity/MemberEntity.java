package io.ssafy.openticon.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "member")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String email;  // 이메일

    @Column(nullable = false, unique = true, length = 255)
    private String nickname;  // 닉네임 (유니크)

    @Builder.Default
    private int point = 0;  // 보유 포인트

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private OffsetDateTime createdAt = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toOffsetDateTime();  // 생성 시간

    @Column(nullable = false)
    @Builder.Default
    private OffsetDateTime updatedAt = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toOffsetDateTime();  // 수정 시간

    @Builder.Default
    private Boolean manager = false;  // 관리자 여부

    @Builder.Default
    private Boolean isResigned = false;  // 탈퇴 여부

    @Builder.Default
    private String mobile_fcm = "";  // 모바일 FCM 토큰

    @Builder.Default
    private String web_fcm = "";  // 웹 FCM 토큰

    @Builder.Default
    private String profile_image = "";  // 프로필 이미지
}
