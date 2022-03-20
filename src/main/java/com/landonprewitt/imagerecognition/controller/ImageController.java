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

import java.io.IOException;
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

    @Operation(summary = "Post a new Image",
            description = "Send a JSON request body including an image file or URL, an optional label for the\n" +
                    "image, and an optional field to enable object detection.\n Returns a HTTP 200 OK with a JSON response body including the image data, its label\n" +
                    "(generate one if the user did not provide it), its identifier provided by the persistent data\n" +
                    "store, and any objects detected (if object detection was enabled).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image Saved",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Image.class))}),
            @ApiResponse(responseCode = "400", description = "Missing Image File or Image URL",
                    content = {@Content()})
    })
    @PostMapping(path = "", produces = "application/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Image> postImage(
            @RequestPart(value = "image") Image image,
            @RequestPart(required = false, value = "imageFile") MultipartFile imageFile) throws IOException {
        // TODO :: Get working in swagger-ui
        // TODO :: Make POJO/DTO for Image Object
        if (imageFile == null && (image == null || image.getUrl().isEmpty())) {
            throw new MissingImageDataException("Need at least one: Image File or Image URL");
        } return ResponseEntity.ok(imageService.addImage(image, imageFile));
    }

}
