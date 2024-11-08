ALTER TABLE emoticon_pack
    ADD COLUMN IF NOT EXISTS download int DEFAULT 0;

