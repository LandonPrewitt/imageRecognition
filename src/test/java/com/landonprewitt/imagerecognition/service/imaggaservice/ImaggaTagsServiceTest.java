package com.landonprewitt.imagerecognition.service.imaggaservice;

import com.landonprewitt.imagerecognition.config.ImaggaConfig;
import com.landonprewitt.imagerecognition.exception.ImaggaImageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.web.client.RestTemplateBuilder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ImaggaTagsServiceTest {

    private ImaggaTagsService imaggaTagsService;

    @Mock
    private ImaggaConfig imaggaConfig;

    @BeforeEach
    void init() {
        // Stubs
        doReturn("user").when(imaggaConfig).getUser();
        doReturn("password").when(imaggaConfig).getPassword();
        imaggaTagsService = mock(ImaggaTagsService.class, withSettings().useConstructor(new RestTemplateBuilder(), imaggaConfig));
        when(imaggaTagsService.parseTagsResponse(any())).thenCallRealMethod();
    }

    @Nested
    @DisplayName("Parse Tags Response Tests")
    class ParseTagsResponseTest {

        @Test
        void ImaggaImageExceptionTest() {
            String response = "HttpResponse Exception Lorem Ipsum blah blah blah";
            assertThrows(ImaggaImageException.class, () -> imaggaTagsService.parseTagsResponse(response));
        }
    }
}
