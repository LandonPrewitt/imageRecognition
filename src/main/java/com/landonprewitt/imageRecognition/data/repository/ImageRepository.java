package com.landonprewitt.imageRecognition.data.repository;

import com.landonprewitt.imageRecognition.data.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    @Query(value = "SELECT * FROM image where id=?1", nativeQuery = true)
    Optional<Image> findImageById(Integer id);

}
