package com.landonprewitt.imagerecognition.service.entityservice;

import com.landonprewitt.imagerecognition.data.entity.DetectedObject;
import com.landonprewitt.imagerecognition.data.entity.Image;
import com.landonprewitt.imagerecognition.data.repository.ImageRepository;
import com.landonprewitt.imagerecognition.exception.ImageNotFoundException;
import com.landonprewitt.imagerecognition.exception.ObjectQueryException;
import com.landonprewitt.imagerecognition.service.imaggaservice.ImaggaTagsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ImageService {

    private ImageRepository imageRepository;
    private ImaggaTagsService imaggaTagsService;
    private DetectedObjectService detectedObjectService;

    public Image addImage(Image image, MultipartFile imageFile) throws IOException {

        // Add tags if required
        if (image.isObjectionDetectionEnabled() && !(image.getUrl().isEmpty() && imageFile == null )) {
            List<DetectedObject> detectedObjects = (!image.getUrl().isEmpty()) ?
                    detectedObjectService.addObjectsByNames(imaggaTagsService.getTagsByURL(image.getUrl())) :
                    detectedObjectService.addObjectsByNames(imaggaTagsService.getTagsByFile(imageFile));
            image.addObjects(detectedObjects);
        }

        // Get the Image Data
        if (image.getUrl().isEmpty() && imageFile != null) {
            image.setImageData(Base64.getEncoder().encode(imageFile.getBytes()));
        }

        // Provide Default Label -- If tags exists, uses first in list
        if (image.getLabel() == null || image.getLabel().isEmpty()) {
            if (image.getDetectedObjects().isEmpty()) image.setLabel("Default Label");
            image.setLabel(image.getDetectedObjects().get(0).getName());
        }

        // Save and return completed Image
        return imageRepository.save(image);
    }

    public List<Image> findAll() {
        return imageRepository.findAll();
    }

    public Image findById(Integer imageId) {
        log.info(String.format("getImageById: %s", imageId));
        return imageRepository.findImageById(imageId).orElseThrow(
                () -> new ImageNotFoundException("Image Not Found by id : " + imageId));
    }

    public List<Image> findByObjects(String objects) {
        log.info(String.format("Find By objects = %s", objects));
        List<String> objectNames = parseObjectQuery(objects);
        List<DetectedObject> detectedObjects = detectedObjectService.findObjectsByName(objectNames);
        return detectedObjects.stream()
                .flatMap(obj -> obj.getImages().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> parseObjectQuery(String objects) {
        if (objects.startsWith("\"") && objects.endsWith("\"")) {
           return Arrays.stream(objects.split(","))
                   .map(name -> name
                           .toLowerCase(Locale.ROOT)
                           .replace("\"", "")
                           .trim())
                   .collect(Collectors.toList());
        } else {
            throw new ObjectQueryException(String.format("Improper Object Query Format - Missing Quotation Marks: %s", objects));
        }
    }

}
