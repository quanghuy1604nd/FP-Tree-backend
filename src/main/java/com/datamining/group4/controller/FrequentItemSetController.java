package com.datamining.group4.controller;

import com.datamining.group4.dto.FrequentItemSetDTO;
import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.ItemSet;
import com.datamining.group4.entity.Node;
import com.datamining.group4.service.FPTreeService;
import com.datamining.group4.service.FileService;
import com.datamining.group4.service.FrequentItemSetService;
import com.datamining.group4.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/frequent-items")
    public FrequentItemSetDTO getFrequentItemSets(@RequestParam String fileName,
                                                  @RequestParam(required = false) Optional<Double> minSup,
                                                  @RequestParam(required = false) Optional<Double> minConf) {

        String filePath = storageService.getPathToFile(fileName);
        List<ItemSet> dataset = fileService.findAll(filePath);

        // bắt đầu tính thời gian cho thuật toán
        long start = System.currentTimeMillis();
        // mặc dịnh ban đầu tần suất của mỗi giao dịch là 1
        List<Integer> frequencies = Collections.nCopies(dataset.size(), 1);
        // tạo nút gốc
        Node rootEntity = new Node("root", 0, null);
        LinkedHashMap<String, Node> headerTableEntity = new LinkedHashMap<>();
        FPTree fpTree = new FPTree(rootEntity, headerTableEntity, minSup.orElse(0.02), dataset.size());
        fpTreeService.constructTree(fpTree, dataset, frequencies);

        FrequentItemSetDTO frequentItemSets = frequentItemSetService.generateFrequentItemSets(fpTree, dataset, minConf.orElse(0.5));
        long duration = System.currentTimeMillis() - start;
        frequentItemSets.setDuration(duration);
        return frequentItemSets;
    }
}
