CREATE TABLE "objection_submit" (
            "id"	bigint	NOT NULL,
            "objection_id"	BIGINT	NOT NULL,
            "content"	TEXT	NULL,
            "created_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT "FK_Objection_TO_Objection_Submit" FOREIGN KEY ("objection_id") REFERENCES "objection" ("id")
);