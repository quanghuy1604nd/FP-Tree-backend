package com.datamining.group4.service;

import com.datamining.group4.dto.FPTreeDTO;
import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.Itemset;
import com.datamining.group4.entity.Node;

import java.util.HashMap;
import java.util.List;

public interface PreprocessingService {
    HashMap<String, Integer> findItemFrequencies(List<Itemset> dataset, List<Integer> frequencies);
    HashMap<String, Integer> findItemGreaterOrEqualThreshold(List<Itemset> dataset, List<Integer> frequencies, int threshold);
    List<Itemset> updateTransactionsAfterRemoveItem(List<Itemset> dataset, List<Integer> frequencies, int threshold);
}
