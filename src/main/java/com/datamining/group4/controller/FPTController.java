package com.datamining.group4.controller;

import com.datamining.group4.dto.FPTreeDTO;
import com.datamining.group4.dto.FrequentItemsetDTO;
import com.datamining.group4.dto.ItemsetDTO;
import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.Itemset;
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
@RequestMapping("/v1/api")
public class FPTController {
    @Autowired
    private PreprocessingService preprocessingService;
    @Autowired
    private FileService fileService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private FPTreeService fpTreeService;

    private FPTree createTreeEntity(String fileName, Optional<Double> minSup) {
        String filePath = storageService.getPathToFile(fileName);
        List<Itemset> dataset = fileService.findAll(filePath);

//        System.out.println(dataset.size() * minSup.get());
        List<Integer> frequencies = Collections.nCopies(dataset.size(), 1);
        Node rootEntity = new Node("root", 0, null);
        LinkedHashMap<String, Node> headerTableEntity = new LinkedHashMap<>();
        FPTree fpTree = new FPTree(rootEntity, headerTableEntity, minSup.orElse(0.02), dataset.size());
        fpTreeService.constructTree(fpTree, dataset, frequencies);
        return  fpTree;
    }

    @GetMapping("/detail")
    public HashMap<String, Integer> getDetailData(@RequestParam String fileName) {
        String filePath = storageService.getPathToFile(fileName);
        List<Itemset> dataset = fileService.findAll(filePath);
        List<Integer> frequencies = Collections.nCopies(dataset.size(), 1);
        return preprocessingService.findItemFrequencies(dataset, frequencies);
    }

    @GetMapping("/transactions")
    public List<Itemset> getTransaction(@RequestParam String fileName, @RequestParam(required = false) Optional<Integer> numOfRecords) {
        String filePath = storageService.getPathToFile(fileName);
        return numOfRecords.map(num -> fileService.findFirstNItemset(filePath, num)).orElseGet(() -> fileService.findFirstNItemset(filePath, 10));
    }

    @GetMapping("/updated/detail")
    public HashMap<String, Integer> getUpdatedDetailData(@RequestParam String fileName, @RequestParam(required = false) Optional<Double> minSup) {
        String filePath = storageService.getPathToFile(fileName);
        List<Itemset> dataset = fileService.findAll(filePath);
        int threshold = minSup.map(aDouble -> (int) (aDouble * dataset.size())).orElseGet(() -> (int) (0.02 * dataset.size()));
        List<Integer> frequencies = Collections.nCopies(dataset.size(), 1);
        return preprocessingService.findItemGreaterOrEqualThreshold(dataset, frequencies, threshold);
    }

    @GetMapping("/updated/transaction")
    public List<Itemset> getUpdatedTransaction(@RequestParam String fileName, @RequestParam(required = false) Optional<Double> minSup) {
        String filePath = storageService.getPathToFile(fileName);
        List<Itemset> dataset = fileService.findAll(filePath);
        int threshold = minSup.map(aDouble -> (int) (aDouble * dataset.size())).orElseGet(() -> (int) (0.02 * dataset.size()));
        List<Integer> frequencies = Collections.nCopies(dataset.size(), 1);
        return preprocessingService.updateTransactionsAfterRemoveItem(dataset, frequencies, threshold);
    }

    @GetMapping("/create")
    public FPTreeDTO createTree(@RequestParam String fileName, @RequestParam(required = false) Optional<Double> minSup) {
        FPTree fpTree = this.createTreeEntity(fileName, minSup);
        return fpTreeService.convertTree(fpTree);
    }

    @GetMapping("/conditional-pattern")
    public FPTreeDTO createPatterns(@RequestParam String fileName, @RequestParam(required = false) Optional<Double> minSup, @RequestParam String item) {
        FPTree fpTree = this.createTreeEntity(fileName, minSup);
        System.out.println(item);
        Pair<List<Itemset>, List<Integer>> prefix = fpTreeService.findPrefixPathsOfItem(fpTree, item);
        List<Itemset> patterns = prefix.getKey();
        List<Integer> frequencies = prefix.getValue();
        Node rootPatterns = new Node("root", 0, null);
        LinkedHashMap<String, Node> x = new LinkedHashMap<>();

        FPTree conditionTree = new FPTree(rootPatterns, x, fpTree.getMinSup(), fpTree.getSizeOfTransactions());
        fpTreeService.constructTree(conditionTree, patterns, frequencies);
        return fpTreeService.convertTree(conditionTree);
    }

    @GetMapping("/frequent-items")
    public FrequentItemsetDTO getFrequentItemsets(@RequestParam String fileName, @RequestParam(required = false) Optional<Double> minSup) {
        FPTree fpTree = this.createTreeEntity(fileName, minSup);
        return fpTreeService.generateFrequentItemsets(fpTree);
    }

//    public static void main(String[] args) {
//        FPTController fpTree = new FPTController();
//        String fileName = "src/main/resources/static/test.csv";
//        Optional <Double> d = Optional.of(0.17);
//        List<Itemset> dataset = fpTree.getFrequentItemsets(fileName, d);
//        System.out.println(dataset);
//    }
}
