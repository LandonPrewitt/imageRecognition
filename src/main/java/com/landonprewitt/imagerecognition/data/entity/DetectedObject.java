package com.landonprewitt.imagerecognition.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@Table(name = "Detected_Object")
public class DetectedObject {

    @Id
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "detectedObjects")
    private List<Image> images = new ArrayList<>();

    public void addImage(Image image) {
        this.images.add(image);
    }

}
