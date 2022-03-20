package com.landonprewitt.imagerecognition.data.entity;

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
@Slf4j
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    private String label;
    @Builder.Default
    private String url = "";
    @Builder.Default
    private boolean objectionDetectionEnabled = false; // Default = False to mimimize work load
    @ManyToMany
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JoinTable(
            name = "Image_DetectedObject",
            joinColumns = @JoinColumn(name = "image_id"),
            inverseJoinColumns = @JoinColumn(name = "object_name")
    )
    @Builder.Default
    private List<DetectedObject> detectedObjects = new ArrayList<>();
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private byte[] imageData;

    public void addObjects(List<DetectedObject> detectedObjects) {
        this.detectedObjects.addAll(detectedObjects);
    }
}
