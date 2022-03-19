package com.landonprewitt.imagerecognition.service.entityservice;

import com.landonprewitt.imagerecognition.data.entity.DetectedObject;
import com.landonprewitt.imagerecognition.data.repository.DetectedObjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DetectedObjectService {

    private DetectedObjectRepository detectedObjectRepository;

    public DetectedObject addObject(DetectedObject object) {
        object.setName(object.getName().toLowerCase(Locale.ROOT)); // Case Insensitivity
        return detectedObjectRepository.save(object);
    }

    public List<DetectedObject> addObjectsByNames(List<String> objectNames) {
        return detectedObjectRepository.saveAll(objectNames.stream()
                .map(objectName -> DetectedObject.builder()
                    .name(objectName.toLowerCase(Locale.ROOT))
                    .build())
                .collect(Collectors.toList()));
    }

    public List<DetectedObject> findObjectsByName(List<String> objectNames) {
        return detectedObjectRepository.findAllByNameIn(objectNames);
    }

}
