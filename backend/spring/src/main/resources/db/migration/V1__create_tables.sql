CREATE TABLE "member" (
                          "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          "email" VARCHAR(255) NOT NULL,
                          "nickname" VARCHAR(255) NOT NULL,
                          "point" BIGINT NOT NULL DEFAULT 0,
                          "created_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          "updated_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          "manager" BOOLEAN NOT NULL DEFAULT FALSE,
                          "is_resigned" BOOLEAN NOT NULL DEFAULT FALSE,
                          "mobile_fcm" VARCHAR(255) ,
                          "web_fcm" VARCHAR(255),
                          "profile_image" VARCHAR(255)
);

CREATE TABLE "emoticon_pack" (
                                 "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                 "title" VARCHAR(255) UNIQUE NOT NULL,
                                 "member_id" BIGINT NOT NULL,
                                 "is_ai_generated" BOOLEAN NOT NULL DEFAULT FALSE,
                                 "price" INT NOT NULL DEFAULT 0,
                                 "view" BIGINT NOT NULL DEFAULT 0,
                                 "is_public" BOOLEAN NOT NULL DEFAULT TRUE,
                                 "is_blacklist" BOOLEAN NOT NULL DEFAULT FALSE,
                                 "category" ENUM('REAL', 'CHARACTER', 'ENTERTAINMENT', 'TEXT') NOT NULL,
                                 "thumbnail_img" TEXT,
                                 "list_img" TEXT,
                                 "description" TEXT,
                                 "examine" ENUM('IN_PROGRESS', 'COMPLETED') NOT NULL DEFAULT 'IN_PROGRESS',
                                 "share_link" TEXT NOT NULL,
                                 "created_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 "updated_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "permission" (
                              "member_id" BIGINT NOT NULL,
                              "emoticon_pack_id" BIGINT NOT NULL,
                              PRIMARY KEY ("member_id", "emoticon_pack_id"),
                              CONSTRAINT "FK_Member_TO_Permission" FOREIGN KEY ("member_id") REFERENCES "Member" ("id"),
                              CONSTRAINT "FK_EmoticonPack_TO_Permission" FOREIGN KEY ("emoticon_pack_id") REFERENCES "emoticon_pack" ("id")
);

CREATE TABLE "purchase_history" (
                                    "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                    "member_id" BIGINT NOT NULL,
                                    "emoticon_pack_id" BIGINT NOT NULL,
                                    "is_hide" BOOLEAN NOT NULL DEFAULT FALSE,
                                    "created_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    CONSTRAINT "FK_Member_TO_PurchaseHistory" FOREIGN KEY ("member_id") REFERENCES "Member" ("id"),
                                    CONSTRAINT "FK_EmoticonPack_TO_PurchaseHistory" FOREIGN KEY ("emoticon_pack_id") REFERENCES "emoticon_pack" ("id")
);

CREATE TABLE "point_history" (
                                 "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                 "member_id" BIGINT NOT NULL,
                                 "type" ENUM('WITHDRAW', 'DEPOSIT', 'PURCHASE', 'SALE') NOT NULL,
                                 "point" BIGINT NOT NULL,
                                 "created_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 CONSTRAINT "FK_Member_TO_PointHistory" FOREIGN KEY ("member_id") REFERENCES "Member" ("id")
);

CREATE TABLE "tag" (
                       "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       "tag_name" VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE "tag_list" (
                            "emoticon_pack_id" BIGINT NOT NULL,
                            "tag_id" BIGINT NOT NULL,
                            PRIMARY KEY ("emoticon_pack_id", "tag_id"),
                            CONSTRAINT "FK_EmoticonPack_TO_TagList" FOREIGN KEY ("emoticon_pack_id") REFERENCES "emoticon_pack" ("id"),
                            CONSTRAINT "FK_Tag_TO_TagList" FOREIGN KEY ("tag_id") REFERENCES "Tag" ("id")
);

CREATE TABLE "favorites" (
                             "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                             "member_id" BIGINT NOT NULL,
                             "emoticon_id" BIGINT NOT NULL,
                             "created_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "report_history" (
                                  "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                  "member_id" BIGINT NOT NULL,
                                  "emoticon_pack_id" BIGINT NOT NULL,
                                  "description" TEXT NOT NULL DEFAULT '',
                                  "created_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  "deleted_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "objection" (
                             "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                             "emoticon_pack_id" BIGINT NOT NULL,
                             "member_id" BIGINT NOT NULL,
                             "type" ENUM('REPORT', 'REVIEW') NOT NULL DEFAULT 'REPORT',
                             "created_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             "state" ENUM('PENDING', 'RECEIVED', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
                             "completed_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "emoticon" (
                            "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                            "emoticon_pack_id" BIGINT NOT NULL,
                            "order" INT NOT NULL DEFAULT 0,
                            "image_path" TEXT NOT NULL DEFAULT ''
);

CREATE TABLE "answer" (
                          "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          "objection_id" BIGINT NOT NULL,
                          "content" TEXT NOT NULL DEFAULT ''
);
