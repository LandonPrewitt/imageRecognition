package com.landonprewitt.imageRecognition;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ImageRecognitionApplication {

	public static void main(String[] args) {SpringApplication.run(ImageRecognitionApplication.class, args);}

	@Bean
	public OpenAPI openApiConfig() {
		return new OpenAPI().info(apiInfo());
	}

	private Info apiInfo() {
		Info info = new Info();
		info
				.title("Object Recognition API")
				.description("Object Recognition Swagger Open API")
				.version("v1.0.0");
		return info;
	}

}
