package com.bibek.bdfs;

import com.bibek.bdfs.config.RSAKeyRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableConfigurationProperties(RSAKeyRecord.class)
public class BdfsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BdfsApplication.class, args);
	}

	@RestController
	public static class SwaggerRedirectController {
		@GetMapping("/")
		public String redirectToSwagger() {
			return "redirect:/v1/swagger";
		}
	}
}
