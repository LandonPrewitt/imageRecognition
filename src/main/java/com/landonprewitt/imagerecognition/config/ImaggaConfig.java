package com.landonprewitt.imagerecognition.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "imagga")
public class ImaggaConfig {

    private String baseUrl;
    private String user;
    private String password;
    private Double threshold;

}
