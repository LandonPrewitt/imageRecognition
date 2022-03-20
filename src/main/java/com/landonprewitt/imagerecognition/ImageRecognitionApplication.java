package com.landonprewitt.imagerecognition;

import com.landonprewitt.imagerecognition.data.entity.Image;
import com.landonprewitt.imagerecognition.service.entityservice.ImageService;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@SpringBootApplication
@Slf4j
public class ImageRecognitionApplication {

	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext configurableApplicationContext =
				SpringApplication.run(ImageRecognitionApplication.class, args);
		ImageService imageService = configurableApplicationContext.getBean(ImageService.class);

		// Load Sample Data
		Image houseImage = Image.builder()
				.objectionDetectionEnabled(true)
				.url("https://www.rocketmortgage.com/resources-cmsassets/RocketMortgage.com/Article_Images/Large_Images/TypesOfHomes/types-of-homes-hero.jpg")
				.detectedObjects(new ArrayList<>())
				.build();
		imageService.addImage(houseImage, null);
		Image baseballImage = Image.builder()
				.objectionDetectionEnabled(true)
				.label("baseballImage")
				.url("https://sportshub.cbsistatic.com/i/2022/03/10/a157d2e7-71a8-4c6c-87e0-69ba2d32dc59/mlb-lockout-12.png")
				.detectedObjects(new ArrayList<>())
				.build();
		imageService.addImage(baseballImage, null);
		Image basketballImage = Image.builder()
				.objectionDetectionEnabled(true)
				.label("basketballImage")
				.url("https://www.masterclass.com/course-images/attachments/6zx7878KdosvRmjEgdpf39Db?width=400&height=400&fit=cover&dpr=2")
				.detectedObjects(new ArrayList<>())
				.build();
		imageService.addImage(basketballImage, null);

		File file = new File("src/main/resources/static/sampleImages/boat.jpg");
		InputStream stream = new FileInputStream(file);
		MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), MediaType.TEXT_HTML_VALUE, stream);
		Image boatImage = Image.builder()
				.objectionDetectionEnabled(true)
				.label("boatImage")
				.detectedObjects(new ArrayList<>())
				.build();
		imageService.addImage(boatImage, multipartFile);
		log.info("App Ready!");
	}

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
