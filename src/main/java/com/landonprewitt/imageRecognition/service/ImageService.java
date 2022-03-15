package com.landonprewitt.imageRecognition.service;

import com.landonprewitt.imageRecognition.data.entity.DetectedObject;
import com.landonprewitt.imageRecognition.data.entity.Image;
import com.landonprewitt.imageRecognition.data.repository.DetectedObjectRepository;
import com.landonprewitt.imageRecognition.data.repository.ImageRepository;
import com.landonprewitt.imageRecognition.exception.ImageNotFoundException;
import com.landonprewitt.imageRecognition.exception.ObjectQueryException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ImageService {

    private ImageRepository imageRepository;
    private DetectedObjectService detectedObjectService;
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
        List<String> objectNames = parseObjectQuery(objects);
        List<DetectedObject> detectedObjects = detectedObjectService.findObjectsByName(objectNames);

        return images;
    }

    public List<String> parseObjectQuery(String objects) {
        if (objects.charAt(0) == '\"' && objects.charAt(objects.length()-1) == '\"') {
           List<String> objectNames = Arrays.asList(objects
                   .replace("\"", "")
                   .replace(" ", "")
                   .split(","));
           return objectNames;
        } else {
            throw new ObjectQueryException(String.format("Improper Object Query Format: %s", objects));
            // todo : test that this exception returns proper http response
        }
    }

    public Image addObjects(Image image) {

        // todo : make sure object names are case insensitive
        DetectedObject detectedObject = detectedObjectRepository.findAll().get(0);
        Image savedImage = imageRepository.save(image);
        detectedObject.addImage(savedImage);
        detectedObjectRepository.save(detectedObject);
        return savedImage;
    }


}
