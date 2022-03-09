package com.landonprewitt.imageRecognition.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class Image {

    private String id;
    private String label;
    private final String imageSrc;
    private List<String> detectedObjects;
    private boolean objectionDetectionEnabled;

}
