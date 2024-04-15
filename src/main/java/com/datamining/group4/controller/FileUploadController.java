package com.datamining.group4.controller;

import com.datamining.group4.dto.Metadata;
import com.datamining.group4.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/api")
@CrossOrigin("*")
public class FileUploadController {
    @Autowired
    private StorageService storageService;
    @PostMapping("/upload")
    public Metadata uploadFile(@RequestParam MultipartFile file, @RequestParam double minSup) {
        return storageService.storeFile(file, minSup);
    }
}
