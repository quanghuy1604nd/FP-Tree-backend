package com.datamining.group4.service;

import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.Itemset;
import com.datamining.group4.entity.Pair;

import java.util.List;

public interface ItemSetService {
    int getSupport(Itemset itemset, List<Itemset> itemsetList);
    Pair<List<Itemset>, List<Integer>> findPrefixPathsOfItem(FPTree fpTree, String item);
//    List<Itemset> generateFrequentItemSets(FPTree fpTree, List<Itemset> dataset, double minConf);
}
