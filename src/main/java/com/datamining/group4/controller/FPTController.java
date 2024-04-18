package com.datamining.group4.controller;

import com.datamining.group4.dto.FPTreeDTO;
import com.datamining.group4.dto.FrequentItemsetDTO;
import com.datamining.group4.dto.ItemsetDTO;
import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.Itemset;
import com.datamining.group4.entity.Node;
import com.datamining.group4.entity.Pair;
import com.datamining.group4.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/v1/api/tree")
public class FPTController {
    @Autowired
    private PreprocessingService preprocessingService;
    @Autowired
    private FileService fileService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private FPTreeService fpTreeService;
    @Autowired
    private ItemSetService itemSetService;

    private FPTree createTreeEntity(String fileName, Optional<Double> minSup) {
        String filePath = storageService.getPathToFile(fileName);
        List<Itemset> dataset = fileService.findAll(filePath);
        List<Integer> frequencies = Collections.nCopies(dataset.size(), 1);
        Node rootEntity = new Node("root", 0, null);
        LinkedHashMap<String, Node> headerTableEntity = new LinkedHashMap<>();
        FPTree fpTree = new FPTree(rootEntity, headerTableEntity, minSup.orElse(0.02), dataset.size());
        fpTreeService.constructTree(fpTree, dataset, frequencies);
        return  fpTree;
    }

    @GetMapping("/create")
    public FPTreeDTO createTree(@RequestParam String fileName, @RequestParam(required = false) Optional<Double> minSup) {
        FPTree fpTree = this.createTreeEntity(fileName, minSup);
        return fpTreeService.convertTree(fpTree);
    }

    @GetMapping("/create/{item}")
    public FPTreeDTO createPatterns(@PathVariable String item,  @RequestParam String fileName, @RequestParam(required = false) Optional<Double> minSup) {
        FPTree fpTree = this.createTreeEntity(fileName, minSup);
        Pair<List<Itemset>, List<Integer>> prefix = itemSetService.findPrefixPathsOfItem(fpTree, item);
        List<Itemset> patterns = prefix.getKey();
        List<Integer> frequencies = prefix.getValue();
        Node rootPatterns = new Node("root", 0, null);
        LinkedHashMap<String, Node> x = new LinkedHashMap<>();

        FPTree conditionTree = new FPTree(rootPatterns, x, fpTree.getMinSup(), fpTree.getSizeOfTransactions());
        fpTreeService.constructTree(conditionTree, patterns, frequencies);
        return fpTreeService.convertTree(conditionTree);
    }


    @GetMapping("/test")
    public void test() {
    }

}
