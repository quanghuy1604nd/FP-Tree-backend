package com.datamining.group4.controller;

import com.datamining.group4.dto.FrequentItemSetDTO;
import com.datamining.group4.entity.ItemSet;
import com.datamining.group4.service.AprioriService;
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
@RequestMapping("/v1/api")
@CrossOrigin("*")
public class FrequentItemSetAprioriController {

    @Autowired
    private AprioriService aprioriService;
    @Autowired
    private FileService fileService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private PreprocessingService preprocessingService;

    @GetMapping("/frequent-itemSet-apriori")
    public FrequentItemSetDTO getFrequentItemSetsApriori(@RequestParam("fileName") String fileName, @RequestParam("minSup") Optional<Double> minSup) {
        String filePath = storageService.getPathToFile(fileName);

        List<ItemSet> dataset = fileService.findAll(filePath);
        List<Integer> frequencies = Collections.nCopies(dataset.size(), 1);
        HashMap<String, Integer> supportCountOfOneItem = preprocessingService.findItemFrequencies(dataset, frequencies);
        long timeStart = System.currentTimeMillis();
        FrequentItemSetDTO frequentItemSets = aprioriService.generateFrequentItemSets(dataset, minSup.orElse(0.02), supportCountOfOneItem);
        long duration = System.currentTimeMillis() - timeStart;
        frequentItemSets.setDuration(duration);
        System.out.println("Duration: " + duration);
        return frequentItemSets;
    }
}
