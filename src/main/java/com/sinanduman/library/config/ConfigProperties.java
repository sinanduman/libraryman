package com.sinanduman.library.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigProperties {
    private String secretKey;
    private String algorithm;
    private String issuer;
    private String audiance;
}