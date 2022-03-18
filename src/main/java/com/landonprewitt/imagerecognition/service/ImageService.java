package com.landonprewitt.imagerecognition.service;

import com.landonprewitt.imagerecognition.data.entity.DetectedObject;
import com.landonprewitt.imagerecognition.data.entity.Image;
import com.landonprewitt.imagerecognition.data.repository.DetectedObjectRepository;
import com.landonprewitt.imagerecognition.data.repository.ImageRepository;
import com.landonprewitt.imagerecognition.exception.ImageNotFoundException;
import com.landonprewitt.imagerecognition.exception.ObjectQueryException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ImageService {

    private ImageRepository imageRepository;
    private DetectedObjectService detectedObjectService;
    private DetectedObjectRepository detectedObjectRepository;

    public Image addImage(Image image) {
        if (image.isObjectionDetectionEnabled()) {
            DetectedObject detectedObject = detectedObjectRepository.findAll().get(0);
            return addObjects(detectedObject, image);
        }
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
        if (objects.charAt(0) == '\"' && objects.charAt(objects.length()-1) == '\"') {
           return Arrays.asList(objects
                   .toLowerCase(Locale.ROOT)
                   .replace("\"", "")
                   .replace(" ", "")
                   .split(","));
        } else {
            throw new ObjectQueryException(String.format("Improper Object Query Format - Missing Quotation Marks: %s", objects));
        }
    }

    public Image addObjects(DetectedObject object, Image image) {
        image.addObject(object);
        return imageRepository.save(image);
    }


}
