CREATE UNIQUE INDEX unique_public_title ON emoticon_pack (title)
WHERE is_public = true;