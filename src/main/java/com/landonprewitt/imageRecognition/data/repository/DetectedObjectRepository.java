package com.landonprewitt.imageRecognition.data.repository;

import com.landonprewitt.imageRecognition.data.entity.DetectedObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetectedObjectRepository extends JpaRepository<DetectedObject, Integer> {

}
