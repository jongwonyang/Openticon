package io.ssafy.openticon;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
class OpenticonApplicationTests {

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

	@Test
	public void testMethod() {
		// 테스트 코드...
	}

}
