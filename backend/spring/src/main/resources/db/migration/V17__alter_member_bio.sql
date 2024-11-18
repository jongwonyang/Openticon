-- member에 상태 메시지 컬럼 추가
ALTER TABLE member
    ADD COLUMN IF NOT EXISTS bio VARCHAR(255) DEFAULT '';

