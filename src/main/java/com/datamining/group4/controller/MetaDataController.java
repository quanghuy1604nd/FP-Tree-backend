package com.datamining.group4.controller;

import com.datamining.group4.converter.ItemSetConverter;
import com.datamining.group4.dto.ItemSetDTO;
import com.datamining.group4.dto.MetaData;
import com.datamining.group4.entity.ItemSet;
import com.datamining.group4.service.FileService;
import com.datamining.group4.service.PreprocessingService;
import com.datamining.group4.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api/meta")
@CrossOrigin("*")
public class MetaDataController {
    @Autowired
    private PreprocessingService preprocessingService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ItemSetConverter itemSetConverter;
    @GetMapping("")
    public MetaData getMetaData(@RequestParam String fileName, @RequestParam(defaultValue = "20") int numOfRecords) {
        String filePath = storageService.getPathToInputFile(fileName);
        List<ItemSet> dataset = fileService.findAll(filePath);
        List<Integer> frequencies = Collections.nCopies(dataset.size(), 1);
        List<ItemSetDTO> transactions = dataset.subList(0, Math.min(numOfRecords, dataset.size())).stream().map(itemSetConverter::toDto).toList();
        HashMap<String, Integer> itemFrequencies = preprocessingService.findItemFrequencies(dataset, frequencies);
        return new MetaData(dataset.size(), itemFrequencies.keySet().size(), transactions, itemFrequencies);
    }
}
