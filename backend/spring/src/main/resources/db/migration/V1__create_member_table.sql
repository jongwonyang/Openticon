CREATE TABLE member (
                        id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- Auto-incrementing ID
                        email VARCHAR(255) NOT NULL,
                        nickname VARCHAR(255) UNIQUE NOT NULL,
                        point BIGINT DEFAULT 0, -- 포인트의 기본값은 0
                        created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP, -- 생성 시간 (타임스탬프)
                        updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP, -- 수정 시간 (타임스탬프)
                        manager BOOLEAN DEFAULT FALSE, -- 관리자 여부
                        is_resigned BOOLEAN DEFAULT FALSE, -- 탈퇴 여부 (Enum처럼 'Y' 또는 'N')
                        mobile_fcm VARCHAR(255), -- 모바일 FCM 토큰
                        web_fcm VARCHAR(255) -- 웹 FCM 토큰
);

