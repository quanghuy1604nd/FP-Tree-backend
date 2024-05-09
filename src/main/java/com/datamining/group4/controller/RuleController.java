package com.datamining.group4.controller;


import com.datamining.group4.converter.FrequentItemSetConverter;
import com.datamining.group4.dto.RuleDTO;
import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.FrequentItemSet;
import com.datamining.group4.entity.ItemSet;
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
@RequestMapping("/v1/api")
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
    @Autowired
    private FrequentItemSetConverter frequentItemSetConverter;

    @GetMapping("/rules-only")
    public List<RuleDTO> getAllRules(@RequestParam String fileName,
                                     @RequestParam(defaultValue = "0.02") double minSup,
                                     @RequestParam(defaultValue="0.5") double minConf) {
        String filePath = storageService.getPathToInputFile(fileName);
        List<ItemSet> dataset = fileService.findAll(filePath);

        // mặc dịnh ban đầu tần suất của mỗi giao dịch là 1
        List<Integer> frequencies = Collections.nCopies(dataset.size(), 1);
        Node rootEntity = new Node("root", 0, null);
        LinkedHashMap<String, Node> headerTableEntity = new LinkedHashMap<>();
        FPTree fpTree = new FPTree(rootEntity, headerTableEntity, minSup, dataset.size());
        fpTreeService.constructTree(fpTree, dataset, frequencies);
        FrequentItemSet frequentItemSet = frequentItemSetConverter.toEntity(frequentItemSetService.generateFrequentItemSets(fpTree));
        return ruleService.generateAllRules(frequentItemSet.getFrequentItemSet(), dataset, minConf);
    }
}
