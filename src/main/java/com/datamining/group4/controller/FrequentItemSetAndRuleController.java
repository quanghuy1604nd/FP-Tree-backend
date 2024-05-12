package com.datamining.group4.controller;

import com.datamining.group4.converter.FrequentItemSetConverter;
import com.datamining.group4.dto.FrequentItemSetAndRuleDTO;
import com.datamining.group4.dto.FrequentItemSetDTO;
import com.datamining.group4.dto.RuleDTO;
import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.FrequentItemSet;
import com.datamining.group4.entity.ItemSet;
import com.datamining.group4.entity.Node;
import com.datamining.group4.service.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/v1/api")
@CrossOrigin("*")
public class FrequentItemSetAndRuleController {
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

    @GetMapping("/rules")
    public FrequentItemSetAndRuleDTO getAllFrequentItemSetsAndRules(@RequestParam String fileName,
                                     @RequestParam(defaultValue = "0.02") double minSup,
                                     @RequestParam(defaultValue="0.5") double minConf) {
        String filePath = storageService.getPathToInputFile(fileName);
        List<ItemSet> dataset = fileService.findAll(filePath);

        // bắt đầu tính thời gian cho thuật toán
        long start = System.currentTimeMillis();

        // mặc dịnh ban đầu tần suất của mỗi giao dịch là 1
        List<Integer> frequencies = Collections.nCopies(dataset.size(), 1);
        Node rootEntity = new Node("root", 0, null);
        LinkedHashMap<String, Node> headerTableEntity = new LinkedHashMap<>();
        FPTree fpTree = new FPTree(rootEntity, headerTableEntity, minSup, dataset.size());
        fpTreeService.constructTree(fpTree, dataset, frequencies);
        FrequentItemSetDTO frequentItemSetDTO = frequentItemSetService.generateFrequentItemSets(fpTree);
        FrequentItemSet frequentItemSet = frequentItemSetConverter.toEntity(frequentItemSetDTO);
        List<RuleDTO> ruleDTOS = ruleService.generateAllRules(frequentItemSet.getFrequentItemSet(), dataset, minConf);
        long duration = System.currentTimeMillis() - start;
        FrequentItemSetAndRuleDTO frequentItemSetAndRuleDTO = new FrequentItemSetAndRuleDTO(frequentItemSetDTO, ruleDTOS, duration);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        storageService.storeFrequentItemSetsAndRuleFPGrowth(fileName, frequentItemSetAndRuleDTO, minSup, minConf);
        return frequentItemSetAndRuleDTO;

    }
}
