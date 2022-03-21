package com.landonprewitt.imagerecognition.service.entityservice;

import com.landonprewitt.imagerecognition.data.entity.DetectedObject;
import com.landonprewitt.imagerecognition.data.repository.DetectedObjectRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DetectedObjectServiceTest {

    @InjectMocks
    DetectedObjectService detectedObjectService;
    @Mock
    DetectedObjectRepository detectedObjectRepository;
    private final List<String> testNames = new ArrayList<>();

    @BeforeEach
    void init() {
        String name1 = "obj";
        String name2 = "OBJ";
        testNames.add(name1);
        testNames.add(name2);
    }

    @Test
    void AddObjectsByNamesUppercase() {

        // Stub - Returns the result of the stream
        when(detectedObjectRepository.saveAll(any())).thenAnswer(i -> i.getArguments()[0]);
        // Asserts
        for (DetectedObject obj : detectedObjectService.addObjectsByNames(testNames)) {
            assertTrue(StringUtils.isAllLowerCase(obj.getName()),
                    "Object names must have all lowercase charachters." +
                            " THis will fail if passed non-characters as names");
        }
    }

}
