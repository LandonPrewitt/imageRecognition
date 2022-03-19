package com.landonprewitt.imagerecognition.data.repository;

import com.landonprewitt.imagerecognition.data.entity.DetectedObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetectedObjectRepository extends JpaRepository<DetectedObject, Integer> {

    List<DetectedObject> findByNameIn(List<String> names);

    List<DetectedObject> findAllByNameIn(List<String> names);

}
