package com.landonprewitt.imagerecognition.service;

import com.landonprewitt.imagerecognition.data.entity.DetectedObject;
import com.landonprewitt.imagerecognition.data.entity.Image;
import com.landonprewitt.imagerecognition.data.repository.ImageRepository;
import com.landonprewitt.imagerecognition.service.entityservice.DetectedObjectService;
import com.landonprewitt.imagerecognition.service.entityservice.ImageService;
import com.landonprewitt.imagerecognition.service.imaggaservice.ImaggaTagsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@Slf4j
class ImageServiceTests {

    @InjectMocks
    private ImageService imageService;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private ImaggaTagsService imaggaTagsService;
    @Mock
    private DetectedObjectService detectedObjectService;

    private List<String> testTags = new ArrayList<>();
    private List<DetectedObject> testObjects = new ArrayList<>();

    @BeforeEach
    void init() throws IOException {
        // Test Data
        testTags.add("testTag");
        DetectedObject testObject = DetectedObject.builder()
                .name("testObject")
                .build();
        testObjects.add(testObject);

        // Stubs
        doReturn(testTags).when(imaggaTagsService).getTagsByURL(any());
        doReturn(testTags).when(imaggaTagsService).getTagsByFile(any());
        doReturn(testObjects).when(detectedObjectService).addObjectsByNames(any());
        when(imageRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
    }


    @Test
    void addImageWithLabel() throws IOException {
        Image imageWithLabel = Image.builder()
                .url("https://sportshub.cbsistatic.com/i/2022/03/10/a157d2e7-71a8-4c6c-87e0-69ba2d32dc59/mlb-lockout-12.png")
                .label("Baseball")
                .objectionDetectionEnabled(true)
                .build();
        imageService.addImage(imageWithLabel, null);
        assertEquals("Baseball", imageWithLabel.getLabel(), "Label must be unchanges");
        assertEquals(1, imageWithLabel.getDetectedObjects().size());
    }

    @Test
    void addImageWithoutLabel() throws IOException {
        Image imageWithoutLabel = Image.builder()
                .url("https://static01.nyt.com/images/2021/09/14/science/07CAT-STRIPES/07CAT-STRIPES-mediumSquareAt3X-v2.jpg")
                .objectionDetectionEnabled(true)
                .build();
        imageService.addImage(imageWithoutLabel, null);
        assertFalse(imageWithoutLabel.getLabel().isEmpty(), "Label must be auto generated");
    }

    @Test
    void addImageWithoutObjectDetection() throws IOException {
        Image imageWithoutObjectDetection = Image.builder()
                .url("https://www.princeton.edu/sites/default/files/styles/half_2x/public/images/2022/02/KOA_Nassau_2697x1517.jpg?itok=iQEwihUn")
                .objectionDetectionEnabled(false)
                .build();
        imageService.addImage(imageWithoutObjectDetection, null);
        assertEquals("Default Label", imageWithoutObjectDetection.getLabel(),
                "Image with no tags or label should be given label \"Default Label\"");
    }

    @Test
    void addImageUsingFile() throws IOException {
        File file = new File("src/main/resources/static/sampleImages/boat.jpg");
        InputStream stream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), MediaType.TEXT_HTML_VALUE, stream);
        Image imageUsingFile = Image.builder()
                .objectionDetectionEnabled(true)
                .build();
        imageService.addImage(imageUsingFile, multipartFile);
        assertTrue(imageUsingFile.getImageData().length > 0,
                "Image loaded via file should contain Image Data");
    }
}
