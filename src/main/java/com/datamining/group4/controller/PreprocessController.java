package com.datamining.group4.controller;

import com.datamining.group4.entity.ItemSet;
import com.datamining.group4.entity.Pair;
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
@RequestMapping("/v1/api/preprocess")
@CrossOrigin("*")
public class PreprocessController {
    @Autowired
    private PreprocessingService preprocessingService;
    @Autowired
    private FileService fileService;
    @Autowired
    private StorageService storageService;

    @GetMapping("/detail")
    public HashMap<String, Integer> getDetailData(@RequestParam String fileName) {
        String filePath = storageService.getPathToInputFile(fileName);
        List<ItemSet> dataset = fileService.findAll(filePath);
        List<Integer> frequencies = Collections.nCopies(dataset.size(), 1);
        return preprocessingService.findItemFrequencies(dataset, frequencies);
    }

    @GetMapping("/transactions")
    public List<ItemSet> getTransaction(@RequestParam String fileName, @RequestParam(required = false) Optional<Integer> numOfRecords) {
        String filePath = storageService.getPathToInputFile(fileName);
        return numOfRecords.map(num -> fileService.findFirstNItemset(filePath, num)).orElseGet(() -> fileService.findFirstNItemset(filePath, 10));
    }

    @GetMapping("/updated/detail")
    public HashMap<String, Integer> getUpdatedDetailData(@RequestParam String fileName, @RequestParam(required = false) Optional<Double> minSup) {
        String filePath = storageService.getPathToInputFile(fileName);
        List<ItemSet> dataset = fileService.findAll(filePath);
        int threshold = minSup.map(aDouble -> (int) (aDouble * dataset.size())).orElseGet(() -> (int) (0.02 * dataset.size()));
        List<Integer> frequencies = Collections.nCopies(dataset.size(), 1);
        return preprocessingService.findItemGreaterOrEqualThreshold(dataset, frequencies, threshold);
    }

    @GetMapping("/updated/transaction")
    public List<ItemSet> getUpdatedTransaction(@RequestParam String fileName, @RequestParam(required = false) Optional<Double> minSup) {
        String filePath = storageService.getPathToInputFile(fileName);
        List<ItemSet> dataset = fileService.findAll(filePath);
        int threshold = minSup.map(aDouble -> (int) (aDouble * dataset.size())).orElseGet(() -> (int) (0.02 * dataset.size()));
        List<Integer> frequencies = Collections.nCopies(dataset.size(), 1);
        return preprocessingService.updateTransactionsAfterRemoveItem(dataset, frequencies, threshold);
    }

}
