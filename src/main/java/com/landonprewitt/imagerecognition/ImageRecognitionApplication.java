package com.landonprewitt.imagerecognition;

import com.landonprewitt.imagerecognition.data.entity.DetectedObject;
import com.landonprewitt.imagerecognition.data.entity.Image;
import com.landonprewitt.imagerecognition.data.repository.ImageRepository;
import com.landonprewitt.imagerecognition.service.DetectedObjectService;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Locale;

@SpringBootApplication
public class ImageRecognitionApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext configurableApplicationContext =
				SpringApplication.run(ImageRecognitionApplication.class, args);
		ImageRepository imageRepository = configurableApplicationContext.getBean(ImageRepository.class);
		DetectedObjectService detectedObjectService = configurableApplicationContext
				.getBean(DetectedObjectService.class);

		DetectedObject carObject = DetectedObject.builder()
				.name("car")
				.build();
		detectedObjectService.addObject(carObject);
		DetectedObject houseObject = DetectedObject.builder()
				.name("house")
				.build();
		detectedObjectService.addObject(houseObject);
		DetectedObject officeObject = DetectedObject.builder()
				.name("office")
				.build();
		detectedObjectService.addObject(officeObject);
		DetectedObject officeTwoObject = DetectedObject.builder()
				.name("OfFice".toLowerCase(Locale.ROOT))
				.build();
		detectedObjectService.addObject(officeTwoObject);

		String imageData = "image data";
		Image houseImage = Image.builder()
				.imageData(imageData)
				.objectionDetectionEnabled(true)
				.label("houseImage")
				.detectedObjects(new ArrayList<>())
				.build();
		houseImage = imageRepository.save(houseImage);
		Image carImage = Image.builder()
				.imageData(imageData)
				.objectionDetectionEnabled(true)
				.label("carImage")
				.detectedObjects(new ArrayList<>())
				.build();
		carImage = imageRepository.save(carImage);
		Image officeImage = Image.builder()
				.imageData(imageData)
				.objectionDetectionEnabled(true)
				.label("officeImage")
				.detectedObjects(new ArrayList<>())
				.build();
		officeImage = imageRepository.save(officeImage);

		houseImage.addObject(houseObject);
		carImage.addObject(carObject);
		officeImage.addObject(officeObject);

		houseImage = imageRepository.save(houseImage);
		imageRepository.save(carImage);
		imageRepository.save(officeImage);

		houseImage.addObject(carObject);
		imageRepository.save(houseImage);
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
