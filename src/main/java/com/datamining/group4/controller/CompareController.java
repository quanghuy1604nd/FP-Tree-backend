package com.datamining.group4.controller;

import com.datamining.group4.dto.CompareResponseDTO;
import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.ItemSet;
import com.datamining.group4.entity.Node;
import com.datamining.group4.service.CompareService;
import com.datamining.group4.service.FPTreeService;
import com.datamining.group4.service.FileService;
import com.datamining.group4.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/v1/api/compare")
public class CompareController {

    @Autowired
    private StorageService storageService;
    @Autowired
    private FileService fileService;
    @Autowired
    private FPTreeService fpTreeService;
    @Autowired
    private CompareService compareService;
    @GetMapping
    public CompareResponseDTO compare(@RequestParam String fileName) {
        String filePath = storageService.getPathToInputFile(fileName);
        List<ItemSet> dataset = fileService.findAll(filePath);
        return compareService.compare(dataset);
    }
}
