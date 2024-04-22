package com.datamining.group4.service;

import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.ItemSet;
import com.datamining.group4.entity.Pair;

import java.util.List;

public interface ItemSetService {
    int getSupport(ItemSet itemset, List<ItemSet> itemSetList);
    Pair<List<ItemSet>, List<Integer>> findPrefixPathsOfItem(FPTree fpTree, String item);
//    List<ItemSet> generateFrequentItemSets(FPTree fpTree, List<ItemSet> dataset, double minConf);
}
