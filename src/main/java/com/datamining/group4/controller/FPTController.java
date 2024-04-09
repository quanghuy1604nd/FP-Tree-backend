package com.datamining.group4.controller;

import com.datamining.group4.dto.FPTreeDTO;
import com.datamining.group4.entity.Node;
import com.datamining.group4.service.PreprocessingService;

import com.datamining.group4.service.FileService;
import com.datamining.group4.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class FPTController {
    @Autowired
    private PreprocessingService preprocessingService;
    @Autowired
    private FileService fileService;
    @Autowired
    private StorageService storageService;

    @GetMapping("/detail")
    public HashMap<String, Integer> getDetailData(@RequestParam String fileName) {
        String filePath = storageService.getPathToFile(fileName);
        List<List<String>> dataset = fileService.readCsv(filePath);
        return preprocessingService.findItemFrequencies(dataset);
    }

    @GetMapping("/transactions")
    public List<List<String>> getTransaction(@RequestParam String fileName, @RequestParam(required = false) Optional<Integer> numOfRecords) {
        String filePath = storageService.getPathToFile(fileName);
        return numOfRecords.map(num -> fileService.getFirstNRecords(filePath, num)).orElseGet(() -> fileService.getFirstNRecords(filePath, 10));
    }

    @GetMapping("/updated/detail")
    public HashMap<String, Integer> getUpdatedDetailData(@RequestParam String fileName, @RequestParam(required = false) Optional<Double> minSup) {
        String filePath = storageService.getPathToFile(fileName);
        List<List<String>> dataset = fileService.readCsv(filePath);
        int threshold = minSup.map(aDouble -> (int) (aDouble * dataset.size())).orElseGet(() -> (int) (0.02 * dataset.size()));
        return preprocessingService.findItemGreaterOrEqualThreshold(dataset, threshold);
    }

    @GetMapping("/updated/transaction")
    public List<List<String>> getUpdatedTransaction(@RequestParam String fileName, @RequestParam(required = false) Optional<Double> minSup) {
        String filePath = storageService.getPathToFile(fileName);
        List<List<String>> dataset = fileService.readCsv(filePath);
        int threshold = minSup.map(aDouble -> (int) (aDouble * dataset.size())).orElseGet(() -> (int) (0.02 * dataset.size()));
        return preprocessingService.updateTransactionsAfterRemoveItem(dataset, threshold);
    }

    @GetMapping("/create")
    public FPTreeDTO createTree(@RequestParam String fileName, @RequestParam(required = false) Optional<Double> minSup) {
        String filePath = storageService.getPathToFile(fileName);
        List<List<String>> rawData = fileService.readCsv(filePath);
        int threshold = minSup.map(aDouble -> (int) (aDouble * rawData.size())).orElseGet(() -> (int) (0.02 * rawData.size()));
        List<List<String>> dataset = preprocessingService.updateTransactionsAfterRemoveItem(rawData, threshold);
        Node rootEntity = new Node("root", 0, null);

        return preprocessingService.createTree(rootEntity, dataset);
    }

//    public static void main(String[] args) {
//        FPTree fpTree = new FPTreeImpl();
//        String fileName = "src/main/resources/static/test.csv";
//        List<List<String>> dataset = fpTree.readCsv(fileName);
//        HashMap<String, Integer> map = fpTree.preProcessing(dataset, 0);
//        Node root = new Node("root", 0);
//        root = fpTree.createTree(root, dataset);
//        System.out.println(root);
//    }
}
