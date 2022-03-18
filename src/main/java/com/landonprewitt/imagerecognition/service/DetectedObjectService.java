package com.landonprewitt.imagerecognition.service;

import com.landonprewitt.imagerecognition.data.entity.DetectedObject;
import com.landonprewitt.imagerecognition.data.repository.DetectedObjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class DetectedObjectService {

    private DetectedObjectRepository detectedObjectRepository;


    public DetectedObject addObject(DetectedObject object) {
        object.setName(object.getName().toLowerCase(Locale.ROOT)); // Case Insensitivity
        return detectedObjectRepository.save(object);
    }

    public List<DetectedObject> addObjects(List<DetectedObject> objects) {
//        List<DetectedObject> adjustedObjects = objects.stream()
//                .
//                .forEach( object -> object.setName(object.getName().toLowerCase(Locale.ROOT)))

        return detectedObjectRepository.saveAll(objects);
    }

    public List<DetectedObject> findObjectsByName(List<String> objectNames) {
        return detectedObjectRepository.findAllByNameIn(objectNames);
    }

}
