package com.landonprewitt.imageRecognition.service;

import com.landonprewitt.imageRecognition.data.entity.DetectedObject;
import com.landonprewitt.imageRecognition.data.repository.DetectedObjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DetectedObjectService {

    private DetectedObjectRepository detectedObjectRepository;


    public List<DetectedObject> findObjectsByName(List<String> objectNames) {
        // todo : use "IN QUERY' with jpa


    }
}
