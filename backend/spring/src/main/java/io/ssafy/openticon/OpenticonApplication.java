package io.ssafy.openticon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "io.ssafy.openticon")
public class OpenticonApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenticonApplication.class, args);
	}

}
