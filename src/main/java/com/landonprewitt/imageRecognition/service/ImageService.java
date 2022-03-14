package com.landonprewitt.imageRecognition.service;

import com.landonprewitt.imageRecognition.data.entity.DetectedObject;
import com.landonprewitt.imageRecognition.data.entity.Image;
import com.landonprewitt.imageRecognition.data.repository.DetectedObjectRepository;
import com.landonprewitt.imageRecognition.data.repository.ImageRepository;
import com.landonprewitt.imageRecognition.exception.ImageNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ImageService {

    private ImageRepository imageRepository;
    private DetectedObjectRepository detectedObjectRepository;

    @PostConstruct
    public void postConstruct() {
        DetectedObject object = DetectedObject.builder()
                .name("test object")
                .build();
        detectedObjectRepository.save(object);
    }

    public Image addObject(DetectedObject object) {
        return new Image();
    }

    public Image addImage(Image image) {
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
        // todo: make sure not to include duplicates of images
        log.info(String.format("Find By objects = %s", objects));
        List<Image> images = new ArrayList<>();
        return images;
    }

    public List<DetectedObject> parseObjectQuery(String objects) {
        // todo: Parse object query list, checking and filter "" marks
        List<DetectedObject> parsedObjects = new ArrayList<>();
        // Check for "" In the Parameter

        return parsedObjects;
    }

    public Image addObjects(Image image) {
        DetectedObject detectedObject = detectedObjectRepository.findAll().get(0);
        Image savedImage = imageRepository.save(image);
        detectedObject.addImage(savedImage);
        detectedObjectRepository.save(detectedObject);
        return savedImage;
    }


}
