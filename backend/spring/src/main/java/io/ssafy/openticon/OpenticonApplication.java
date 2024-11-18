package io.ssafy.openticon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "io.ssafy.openticon")
@EnableScheduling
public class OpenticonApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenticonApplication.class, args);
	}

}
