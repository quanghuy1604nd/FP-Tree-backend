package com.datamining.group4.controller;


import com.datamining.group4.dto.FrequentItemsetDTO;
import com.datamining.group4.dto.RuleDTO;
import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.Itemset;
import com.datamining.group4.entity.Node;
import com.datamining.group4.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/v1/api/rule")
public class RuleController {
    @Autowired
    private RuleService ruleService;
    @Autowired
    private FPTreeService fpTreeService;
    @Autowired
    private FileService fileService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private FrequentItemSetService frequentItemSetService;

    @GetMapping("/rules")
    public FrequentItemsetDTO getFrequentItemsets(@RequestParam String fileName,
                                                  @RequestParam(required = false) Optional<Double> minSup,
                                                  @RequestParam(required = false) Optional<Double> minConf) {
        String filePath = storageService.getPathToFile(fileName);
        List<Itemset> dataset = fileService.findAll(filePath);
        List<Integer> frequencies = Collections.nCopies(dataset.size(), 1);
        Node rootEntity = new Node("root", 0, null);
        LinkedHashMap<String, Node> headerTableEntity = new LinkedHashMap<>();
        FPTree fpTree = new FPTree(rootEntity, headerTableEntity, minSup.orElse(0.02), dataset.size());
        fpTreeService.constructTree(fpTree, dataset, frequencies);

        return frequentItemSetService.generateFrequentItemSets(fpTree, dataset, minConf.orElse(0.5));

    }
}
