package com.datamining.group4.controller;

import com.datamining.group4.dto.FPTreeDTO;
import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.ItemSet;
import com.datamining.group4.entity.Node;
import com.datamining.group4.entity.Pair;
import com.datamining.group4.service.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileWriter;
import java.io.IOException;
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

    private FPTree createTreeEntity(String fileName, double minSup) {
        String filePath = storageService.getPathToInputFile(fileName);
        List<ItemSet> dataset = fileService.findAll(filePath);
        List<Integer> frequencies = Collections.nCopies(dataset.size(), 1);
        Node rootEntity = new Node("root", 0, null);
        LinkedHashMap<String, Node> headerTableEntity = new LinkedHashMap<>();
        FPTree fpTree = new FPTree(rootEntity, headerTableEntity, minSup, dataset.size());
        fpTreeService.constructTree(fpTree, dataset, frequencies);
        return  fpTree;
    }

    @GetMapping("/create")
    public FPTreeDTO createTree(@RequestParam String fileName,
                                @RequestParam(defaultValue = "0.02") double minSup) {
        FPTree fpTree = this.createTreeEntity(fileName, minSup);
        FPTreeDTO treeDTO = fpTreeService.convertTree(fpTree);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try(FileWriter fileWriter = new FileWriter(storageService.getPathToDirectoryStoreInputFile(fileName)+"/FPG_tree_minSup_"+minSup+".json")) {
            gson.toJson(treeDTO, fileWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return treeDTO;
    }

    @GetMapping("/create/{item}")
    public FPTreeDTO createPatterns(@PathVariable String item,
                                    @RequestParam String fileName,
                                    @RequestParam(defaultValue = "0.02") double minSup) {
        FPTree fpTree = this.createTreeEntity(fileName, minSup);
        Pair<List<ItemSet>, List<Integer>> prefix = itemSetService.findPrefixPathsOfItem(fpTree, item);
        List<ItemSet> patterns = prefix.getKey();
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
