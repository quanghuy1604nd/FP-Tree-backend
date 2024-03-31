package com.datamining.group4.controller;

import com.datamining.group4.converter.NodeConverter;
import com.datamining.group4.dto.NodeDTO;
import com.datamining.group4.entity.Node;
import com.datamining.group4.service.FPTreeService;

import com.datamining.group4.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
@RestController
@CrossOrigin("*")
public class FPTController {
    @Autowired
    private FPTreeService fpTree;

    @Autowired
    private StorageService storageService;

    @GetMapping("/detail")
    public HashMap<String, Integer> getDetailData(@RequestParam(required = false) String fileName) {
        String filePath = storageService.getPathToFile(fileName);
        List<List<String>> dataset = fpTree.readCsv(filePath);
        return fpTree.findItemGreaterOrEqualThreshold(dataset, 0);
    }

    @GetMapping("/updatedtransaction")
    public List<List<String>> getUpdatedTransaction(@RequestParam String fileName) {
        String filePath = storageService.getPathToFile(fileName);
        List<List<String>> dataset = fpTree.readCsv(filePath);
        return fpTree.updateTransactionsAfterRemoveItem(dataset, 0);
    }

    @GetMapping("/create")
    public NodeDTO createTree(@RequestParam String fileName) {
        String filePath = storageService.getPathToFile(fileName);
        List<List<String>> rawData = fpTree.readCsv(filePath);
        List<List<String>> dataset = fpTree.updateTransactionsAfterRemoveItem(rawData, 0);
        Node rootEntity = new Node("root", 0, null);

        return fpTree.createTree(rootEntity, dataset);
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
