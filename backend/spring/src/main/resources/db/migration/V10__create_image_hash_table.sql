CREATE TABLE image_hash (
            id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
            pack_id BIGINT NOT NULL,
            is_thumbnail BOOLEAN,
            is_list_img BOOLEAN,
            emoticon_order INT,
            algorithm_id INT,
            hash_value NUMERIC(38,0),
            hash_length INT,
            CONSTRAINT fk_emoticon_pack
                FOREIGN KEY (pack_id)
                    REFERENCES emoticon_pack(id)  -- EmoticonPackEntity 테이블의 PK를 참조
                    ON DELETE CASCADE
);
