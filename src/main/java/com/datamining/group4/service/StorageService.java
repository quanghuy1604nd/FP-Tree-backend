package com.datamining.group4.service;

import com.datamining.group4.dto.Metadata;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String createUniqueFileName();
    Metadata storeFile(MultipartFile file, double minSup, double minConf);
    String getPathToFile(String fileName);
    boolean isExist(String fileName);
}
