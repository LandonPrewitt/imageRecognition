package com.landonprewitt.imageRecognition.controller;

import com.landonprewitt.imageRecognition.data.entity.Image;
import com.landonprewitt.imageRecognition.data.repository.ImageRepository;
import com.landonprewitt.imageRecognition.exception.ImageNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("images")
@Tag(name = "Images")
@Slf4j
public class ImageController {

    private final ImageRepository imageRepository;

//    @GetMapping(path = "", produces = "application/json")
//    public ResponseEntity<List<Image>> getImages() {
//        return ResponseEntity.ok(imageRepository.getAllImages());
//    }

    @Operation(summary = "Retrieves Images Containing listed Objects",
            method = "/images?objects=\"\"",
            description = "Returns a HTTP 200 OK with a JSON response body containing only images that have\n" +
                    "the detected objects specified in the query parameter. If no objects are specified, it will return" +
                    " all Images")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Images Found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Image.class))}),
            @ApiResponse(responseCode = "404", description = "No Images Found",
                    content = {@Content()})
    })
    @GetMapping(path = "", produces = "application/json")
    public ResponseEntity<List<Image>> getImagesByObjects(@RequestParam(required = false) String objects) {
        log.info(String.format("objects = %s", objects));

        if (objects instanceof String) {
            // todo : find by Objects
            log.info("Instance of String");

        } else if (objects == null) {
            // todo : find ALL images
            log.info("Instance of null");
        } else {
            // todo : return 404
            log.info("getImagesByObjects 404");
        }

        String[] names = {"hello", "hi"};

        return ResponseEntity.ok(imageRepository.findAll());
    }

    @Operation(summary = "Retrieves Images by their ID",
            method = "/images?objects=\"\"",
            description = "Returns HTTP 200 OK with a JSON response containing image metadata for the\n" +
                    "specified image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image Found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Image.class))}),
            @ApiResponse(responseCode = "404", description = "No Image Found by provided ID",
                    content = {@Content()})
    })
    @GetMapping(path = "/{imageId}", produces = "application/json")
    public ResponseEntity<Image> findById(@PathVariable Integer imageId) {
        log.info(String.format("getImageById: %s", imageId));
        Image image = imageRepository.findImageById(imageId).orElseThrow(
                () -> new ImageNotFoundException("Image Not Found by id : " + imageId));
        return ResponseEntity.ok(image);
        //return ResponseEntity.ok(imageRepository.getById(imageId));
    }

    @PostMapping(path = "", produces = "application/json")
    public ResponseEntity<Image> postImage(@RequestBody Image image) {
        return ResponseEntity.ok(imageRepository.save(image));
    }

}
