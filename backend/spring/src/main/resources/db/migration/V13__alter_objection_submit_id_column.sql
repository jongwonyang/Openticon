-- 먼저 "id" 컬럼을 IDENTITY 컬럼으로 변경합니다.
ALTER TABLE "objection_submit"
ALTER COLUMN "id"
ADD GENERATED ALWAYS AS IDENTITY;

-- 다음으로 "id" 컬럼에 PRIMARY KEY 제약 조건을 추가합니다.
ALTER TABLE "objection_submit"
ADD CONSTRAINT "objection_submit_pkey" PRIMARY KEY ("id");