package com.landonprewitt.imagerecognition.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Slf4j
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    private String label;
    private String imageData;
    private boolean objectionDetectionEnabled;

    @ManyToMany
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JoinTable(
            name = "Image_DetectedObject",
            joinColumns = @JoinColumn(name = "image_id"),
            inverseJoinColumns = @JoinColumn(name = "object_name")
    )
    private List<DetectedObject> detectedObjects = new ArrayList<>();

    public void addObject(DetectedObject object) {
        detectedObjects.add(object);
    }

    public void addObjects(List<DetectedObject> detectedObjects) {
        this.getDetectedObjects().addAll(detectedObjects);
    }
}
