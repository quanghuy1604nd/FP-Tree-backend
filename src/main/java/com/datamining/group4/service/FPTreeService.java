package com.datamining.group4.service;

import com.datamining.group4.dto.FPTreeDTO;
import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.Node;
import com.datamining.group4.entity.Pair;

import java.util.List;

public interface FPTreeService {
    FPTreeDTO convertTree(FPTree fpTree);
    FPTree constructTree(FPTree fpTree, List<List<String>> dataset, List<Integer> frequencies);
    List<String> asendFpTree(Node node, String item);
    Pair<List<List<String>>, List<Integer>> findPrefixPathsOfItem(FPTree fpTree, String item);
    List<List<String>> generateFrequentItemsets(FPTree fpTree);
    void mineTree(FPTree fpTree, List<String> prefix, List<List<String>> frequentItemList);

}
