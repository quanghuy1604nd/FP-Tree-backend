package com.datamining.group4.controller;

import com.datamining.group4.dto.FPTreeDTO;
import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.Node;
import com.datamining.group4.entity.Pair;
import com.datamining.group4.service.FPTreeService;
import com.datamining.group4.service.PreprocessingService;

import com.datamining.group4.service.FileService;
import com.datamining.group4.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin("*")
public class FPTController {
    @Autowired
    private PreprocessingService preprocessingService;
    @Autowired
    private FileService fileService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private FPTreeService fpTreeService;

    @GetMapping("/detail")
    public HashMap<String, Integer> getDetailData(@RequestParam String fileName) {
        String filePath = storageService.getPathToFile(fileName);
        List<List<String>> dataset = fileService.readCsv(filePath);
        List<Integer> frequencies = Collections.nCopies(dataset.size(), 1);
        return preprocessingService.findItemFrequencies(dataset, frequencies);
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
        List<Integer> frequencies = Collections.nCopies(dataset.size(), 1);
        return preprocessingService.findItemGreaterOrEqualThreshold(dataset, frequencies, threshold);
    }

    @GetMapping("/updated/transaction")
    public List<List<String>> getUpdatedTransaction(@RequestParam String fileName, @RequestParam(required = false) Optional<Double> minSup) {
        String filePath = storageService.getPathToFile(fileName);
        List<List<String>> dataset = fileService.readCsv(filePath);
        int threshold = minSup.map(aDouble -> (int) (aDouble * dataset.size())).orElseGet(() -> (int) (0.02 * dataset.size()));
        List<Integer> frequencies = Collections.nCopies(dataset.size(), 1);
        return preprocessingService.updateTransactionsAfterRemoveItem(dataset, frequencies, threshold);
    }

    @GetMapping("/create")
    public FPTreeDTO createTree(@RequestParam String fileName, @RequestParam(required = false) Optional<Double> minSup) {
        List<List<String>> dataset = getUpdatedTransaction(fileName, minSup);
        List<Integer> frequencies = Collections.nCopies(dataset.size(), 1);
        int threshold = minSup.map(aDouble -> (int) (aDouble * dataset.size())).orElseGet(() -> (int) (0.02 * dataset.size()));
        Node rootEntity = new Node("root", 0, null);
        LinkedHashMap<String, Node> headerTableEntity = new LinkedHashMap<>();
        FPTree fpTree = new FPTree(rootEntity, headerTableEntity, threshold);
        fpTree.createTree(dataset, frequencies);
        return fpTreeService.convertTree(fpTree);
    }

    @GetMapping("/a")
    public List<List<String>> con(@RequestParam String fileName, @RequestParam(required = false) Optional<Double> minSup) {
        String filePath = storageService.getPathToFile(fileName);
        List<List<String>> rawData = fileService.readCsv(filePath);
        List<Integer> rawFrequencies = Collections.nCopies(rawData.size(), 1);

        int threshold = minSup.map(aDouble -> (int) (aDouble * rawData.size())).orElseGet(() -> (int) (0.02 * rawData.size()));
        List<List<String>> dataset = preprocessingService.updateTransactionsAfterRemoveItem(rawData, rawFrequencies, threshold);
        List<Integer> frequencies = Collections.nCopies(dataset.size(), 1);

        Node rootEntity = new Node("root", 0, null);
        LinkedHashMap<String, Node> headerTableEntity = new LinkedHashMap<>();
        FPTree fpTree = new FPTree(rootEntity, headerTableEntity, threshold);
        fpTree.createTree(dataset, frequencies);
        return fpTreeService.findPrefixPathsOfItem(fpTree, "p").getKey();
    }

    @GetMapping("/conditional-pattern")
    public FPTreeDTO createPatterns(@RequestParam String fileName, @RequestParam(required = false) Optional<Double> minSup, @RequestParam String item) {
        List<List<String>> dataset = getUpdatedTransaction(fileName, minSup);
        List<Integer> frequenciesOfWholeTree = Collections.nCopies(dataset.size(), 1);

        int threshold = minSup.map(aDouble -> (int) (aDouble * dataset.size())).orElseGet(() -> (int) (0.02 * dataset.size()));
        Node rootEntity = new Node("root", 0, null);
        LinkedHashMap<String, Node> headerTableEntity = new LinkedHashMap<>();
        FPTree fpTree = new FPTree(rootEntity, headerTableEntity, threshold);
        fpTree.createTree(dataset, frequenciesOfWholeTree);


        List<List<String>> patterns = fpTreeService.findPrefixPathsOfItem(fpTree, item).getKey();
        System.out.println(patterns);
        Node rootPatterns = new Node("root", 0, null);
        LinkedHashMap<String, Node> x = new LinkedHashMap<>();

        FPTree conditionTree = new FPTree(rootPatterns, x, 0);
        conditionTree.createConditionalPatternsBase(patterns);
        return fpTreeService.convertTree(conditionTree);
    }

    @GetMapping("/frequent-items")
    public List<List<String>> getFrequentItemsets(@RequestParam String fileName, @RequestParam(required = false) Optional<Double> minSup) {
        String filePath = storageService.getPathToFile(fileName);
        List<List<String>> rawData = fileService.readCsv(filePath);
        int threshold = minSup.map(aDouble -> (int) (aDouble * rawData.size())).orElseGet(() -> (int) (0.02 * rawData.size()));
        List<Integer> frequencies = Collections.nCopies(rawData.size(), 1);
        List<List<String>> dataset = preprocessingService.updateTransactionsAfterRemoveItem(rawData, frequencies, threshold);

        Node rootEntity = new Node("root", 0, null);
        LinkedHashMap<String, Node> headerTableEntity = new LinkedHashMap<>();
        FPTree fpTree = new FPTree(rootEntity, headerTableEntity, threshold);
        fpTree.createTree(dataset, frequencies);
        return fpTreeService.generateFrequentItemsets(fpTree);
    }

//    public static void main(String[] args) {
//        FPTController fpTree = new FPTController();
//        String fileName = "src/main/resources/static/test.csv";
//        Optional <Double> d = Optional.of(0.17);
//        List<List<String>> dataset = fpTree.getFrequentItemsets(fileName, d);
//        System.out.println(dataset);
//    }
}
