package com.datamining.group4.controller;

import com.datamining.group4.dto.MetaFile;
import com.datamining.group4.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/api/storage")
@CrossOrigin("*")
public class StorageController {
    @Autowired
    private StorageService storageService;
    @PostMapping("/upload")
    public MetaFile uploadFile(@RequestParam MultipartFile file, @RequestParam double minSup, @RequestParam double minConf) {
        return storageService.storeInputFile(file, minSup, minConf);
    }
}
