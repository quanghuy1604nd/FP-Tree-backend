package com.datamining.group4.controller;

import com.datamining.group4.dto.FrequentItemSetDTO;
import com.datamining.group4.entity.DataEntity;
import com.datamining.group4.service.AprioriService;
import com.datamining.group4.service.FileService;
import com.datamining.group4.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/frequent-itemSet-apriori")
    public FrequentItemSetDTO getFrequentItemSetsApriori(@RequestParam("fileName") String fileName, @RequestParam("minSup") Optional<Double> minSup) {
        String filePath = storageService.getPathToFile(fileName);
        DataEntity dataset = fileService.findAllTransactions(filePath);

        long timeStart = System.currentTimeMillis();
        FrequentItemSetDTO frequentItemSets = aprioriService.generateFrequentItemSets(dataset.getListItemSet(), minSup.orElse(0.02), dataset.getSupportCount());
        long duration = System.currentTimeMillis() - timeStart;
        frequentItemSets.setDuration(duration);
        return frequentItemSets;
    }
}
