package com.datamining.group4.service;

import com.datamining.group4.dto.FPTreeDTO;
import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.ItemSet;

import java.util.List;
import java.util.Set;

public interface FPTreeService {
    FPTreeDTO convertTree(FPTree fpTree);
    void constructTree(FPTree fpTree, List<ItemSet> dataset, List<Integer> frequencies);
    void mineTree(FPTree fpTree, Set<String> prefix, List<ItemSet> frequentItemList);
}
