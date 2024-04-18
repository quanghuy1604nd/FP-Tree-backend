package com.datamining.group4.service;

import com.datamining.group4.dto.FPTreeDTO;
import com.datamining.group4.dto.FrequentItemsetDTO;
import com.datamining.group4.dto.RuleDTO;
import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.Itemset;
import com.datamining.group4.entity.Node;
import com.datamining.group4.entity.Pair;

import java.util.List;
import java.util.Set;

public interface FPTreeService {
    FPTreeDTO convertTree(FPTree fpTree);
    void constructTree(FPTree fpTree, List<Itemset> dataset, List<Integer> frequencies);
    void mineTree(FPTree fpTree, Set<String> prefix, List<Itemset> frequentItemList);
}
