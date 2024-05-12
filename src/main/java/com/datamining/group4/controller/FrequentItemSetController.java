package com.datamining.group4.controller;

import com.datamining.group4.dto.FrequentItemSetDTO;
import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.ItemSet;
import com.datamining.group4.entity.Node;

import com.datamining.group4.service.*;

import com.datamining.group4.service.FPTreeService;
import com.datamining.group4.service.FileService;
import com.datamining.group4.service.FrequentItemSetService;
import com.datamining.group4.service.StorageService;
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
public class FrequentItemSetController {
    @Autowired
    private FPTreeService fpTreeService;
    @Autowired
    private FileService fileService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private FrequentItemSetService frequentItemSetService;
    @Autowired
    private AprioriService aprioriService;


    @GetMapping("/frequent-items")
    public FrequentItemSetDTO getFrequentItemSets(@RequestParam String fileName,
                                                  @RequestParam(defaultValue = "0.02") double minSup,
                                                  @RequestParam(defaultValue="0.5") double minConf) {

        String filePath = storageService.getPathToInputFile(fileName);
        List<ItemSet> dataset = fileService.findAll(filePath);

        // bắt đầu tính thời gian cho thuật toán
        long start = System.currentTimeMillis();
        // mặc dịnh ban đầu tần suất của mỗi giao dịch là 1
        List<Integer> frequencies = Collections.nCopies(dataset.size(), 1);
        // tạo nút gốc
        Node rootEntity = new Node("root", 0, null);
        LinkedHashMap<String, Node> headerTableEntity = new LinkedHashMap<>();
        FPTree fpTree = new FPTree(rootEntity, headerTableEntity, minSup, dataset.size());
        fpTreeService.constructTree(fpTree, dataset, frequencies);
        FrequentItemSetDTO frequentItemSets = frequentItemSetService.generateFrequentItemSets(fpTree);
        frequentItemSets.setFrequentItemSet(frequentItemSets.getFrequentItemSet().stream().peek(x -> x.setSupport(x.getSupport() / fpTree.getSizeOfTransactions())).toList());
        long duration = System.currentTimeMillis() - start;
        frequentItemSets.setDuration(duration);
        return frequentItemSets;
    }

}
