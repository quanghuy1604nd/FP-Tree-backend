package com.datamining.group4.controller;

import com.datamining.group4.entity.CSVFile;
import com.datamining.group4.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/api")
@CrossOrigin("*")
public class FileUploadController {
    @Autowired
    private StorageService storageService;
    @PostMapping("/upload")
    public CSVFile uploadFile(@RequestParam MultipartFile file) {
        return storageService.storeFile(file);
    }
}
