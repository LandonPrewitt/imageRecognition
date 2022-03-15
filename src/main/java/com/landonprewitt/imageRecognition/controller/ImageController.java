package com.landonprewitt.imageRecognition.controller;

import com.landonprewitt.imageRecognition.data.entity.DetectedObject;
import com.landonprewitt.imageRecognition.data.entity.Image;
import com.landonprewitt.imageRecognition.data.repository.DetectedObjectRepository;
import com.landonprewitt.imageRecognition.data.repository.ImageRepository;
import com.landonprewitt.imageRecognition.exception.ImageNotFoundException;
import com.landonprewitt.imageRecognition.service.ImageService;
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

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("images")
@Tag(name = "Images")
@Slf4j
public class ImageController {

    private final ImageService imageService;

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
        if (objects.isEmpty()) return ResponseEntity.ok(imageService.findAll());
        return ResponseEntity.ok(imageService.findByObjects(objects));
        // todo : if no images returned, provided 404
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
        return ResponseEntity.ok(imageService.findById(imageId));
        // todo : test image not found status using swagger
    }

    @PostMapping(path = "", produces = "application/json")
    public ResponseEntity<Image> postImage(@RequestBody Image image) {
        return ResponseEntity.ok(imageService.addImage(image));
    }

}
