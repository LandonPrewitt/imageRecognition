package com.landonprewitt.imagerecognition.controller;

import com.landonprewitt.imagerecognition.data.entity.Image;
import com.landonprewitt.imagerecognition.exception.ImageNotFoundException;
import com.landonprewitt.imagerecognition.exception.MissingImageDataException;
import com.landonprewitt.imagerecognition.service.entityservice.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
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
                    content = {@Content()}),
            @ApiResponse(responseCode = "406", description = "Unacceptable Query Format",
                    content = {@Content()})
    })
    @GetMapping(path = "", produces = "application/json")
    public ResponseEntity<List<Image>> getImagesByObjects(@RequestParam(required = false) String objects) {
        List<Image> foundImages =
                (objects == null || objects.isEmpty()) ? imageService.findAll() : imageService.findByObjects(objects);
        if (foundImages.isEmpty()) throw new ImageNotFoundException("No Images found");
        return ResponseEntity.ok(foundImages);
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
    }

    @PostMapping(path = "", produces = "application/json", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            "multipart/form-data",
            "image/jpeg",
            "image/jpg"
    })
    public ResponseEntity<Image> postImage(
            @RequestPart(value = "image") Image image,
            @RequestPart(required = false, value = "imageFile") MultipartFile imageFile) throws IOException {
        if (imageFile == null && (image == null || image.getUrl().isEmpty())) {
            throw new MissingImageDataException("Need at least one: Image File or Image URL");
        } return ResponseEntity.ok(imageService.addImage(image, imageFile));
    }

}
