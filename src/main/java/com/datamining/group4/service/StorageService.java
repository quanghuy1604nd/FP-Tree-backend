package com.datamining.group4.service;

import com.datamining.group4.entity.CSVFile;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String createUniqueFileName();
    CSVFile storeFile(MultipartFile file);

    String getPathToFile(String fileName);
}
