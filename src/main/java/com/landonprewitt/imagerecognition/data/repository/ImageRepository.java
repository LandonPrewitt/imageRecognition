package com.landonprewitt.imagerecognition.data.repository;

import com.landonprewitt.imagerecognition.data.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    Optional<Image> findImageById(Integer id);

}
