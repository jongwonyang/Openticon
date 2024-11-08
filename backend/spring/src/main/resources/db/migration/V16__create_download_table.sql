CREATE TABLE "download" (
                            "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                            "emoticon_pack_id" BIGINT NOT NULL,
                            "count"	INT NOT NULL DEFAULT 0
);
