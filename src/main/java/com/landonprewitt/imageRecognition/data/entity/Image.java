package com.landonprewitt.imageRecognition.data.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Slf4j
public class Image {

    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String label;
    private String imageData;
    private boolean objectionDetectionEnabled;

    @ManyToMany(mappedBy = "images")
    @JsonIgnore
    private Set<DetectedObject> detectedObjects = new HashSet<>();

    public void addObject(DetectedObject object) {
        detectedObjects.add(object);
    }

    public void addObjects(List<DetectedObject> detectedObjects) {
        log.info(String.format("Adding Object to image: %s", detectedObjects.get(0).getName()));
        this.getDetectedObjects().addAll(detectedObjects);
        log.info(String.format("Objects added: " + this.detectedObjects));
    }
}
