package io.ssafy.openticon;

import io.github.cdimascio.dotenv.Dotenv;
import io.ssafy.openticon.entity.TagEntity;
import io.ssafy.openticon.repository.TagRepository;
import io.ssafy.openticon.service.TagService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class TagServiceTest {
    @BeforeAll
    static void setUp() {
        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .filename(".env")
                .load();

        System.setProperty("LOCAL_NAME", dotenv.get("LOCAL_NAME"));
        System.setProperty("LOCAL_PASSWORD", dotenv.get("LOCAL_PASSWORD"));

        System.setProperty("KAKAO_OAUTH_CLIENT_ID", dotenv.get("KAKAO_OAUTH_CLIENT_ID"));
        System.setProperty("KAKAO_OAUTH_CLIENT_SECRET", dotenv.get("KAKAO_OAUTH_CLIENT_SECRET"));
        System.setProperty("NAVER_OAUTH_CLIENT_ID", dotenv.get("NAVER_OAUTH_CLIENT_ID"));
        System.setProperty("NAVER_OAUTH_CLIENT_SECRET", dotenv.get("NAVER_OAUTH_CLIENT_SECRET"));
        System.setProperty("GOOGLE_OAUTH_CLIENT_ID", dotenv.get("GOOGLE_OAUTH_CLIENT_ID"));
        System.setProperty("GOOGLE_OAUTH_CLIENT_SECRET", dotenv.get("GOOGLE_OAUTH_CLIENT_SECRET"));

        System.setProperty("JWT_SECRET_KEY", dotenv.get("JWT_SECRET_KEY"));
        System.setProperty("IAMPORT_API_KEY", dotenv.get("IAMPORT_API_KEY"));
        System.setProperty("IAMPORT_API_SECRET", dotenv.get("IAMPORT_API_SECRET"));
        System.setProperty("LOCAL_BASE_URL", dotenv.get("LOCAL_BASE_URL"));
    }

    @Autowired
    private TagService tagService;

    @Autowired
    private TagRepository tagRepository;

    @Test
    public void addTag_새로운태그_저장() {
        // given
        String tagName = "새로운태그";

        // when
        tagService.addTag(tagName);

        // then
        Optional<TagEntity> findTag = tagRepository.findByTagName(tagName);
        assertThat(findTag).isPresent();
        assertThat(findTag.get().getTagName()).isEqualTo(tagName);
    }

    @Test
    public void addTag_이미존재하는태그_저장안함() {
        // given
        String tagName = "이미존재태그";
        TagEntity tagEntity = TagEntity.builder()
                .tagName(tagName)
                .build();
        tagRepository.save(tagEntity);

        // when
        tagService.addTag(tagName);

        // then
        long count = tagRepository.countByTagName(tagName);
        assertThat(count).isEqualTo(1);
    }
}
