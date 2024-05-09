package com.datamining.group4.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Component
@ConfigurationProperties(prefix = "storage")
@Data
public class StorageProperties {
    private String location;
}
