package com.landonprewitt.imageRecognition.data.repository;

import com.landonprewitt.imageRecognition.data.entity.Image;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ImageRepository {

    public List<Image> getAllImages() {
        List<Image> images = new ArrayList<Image>();
        images.add(new Image("image Source"));
        return images;
    }

    public Image saveImage(Image image) {
        return image;
    }

}
