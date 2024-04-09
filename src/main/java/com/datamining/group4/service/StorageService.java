package com.datamining.group4.service;

import com.datamining.group4.entity.Metadata;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String createUniqueFileName();
    Metadata storeFile(MultipartFile file, double minSup);

    String getPathToFile(String fileName);
}
