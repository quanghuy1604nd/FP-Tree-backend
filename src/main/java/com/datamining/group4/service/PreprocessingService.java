package com.datamining.group4.service;

import com.datamining.group4.dto.FPTreeDTO;
import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.Node;

import java.util.HashMap;
import java.util.List;

public interface PreprocessingService {
    FPTreeDTO createTree(Node root, List<List<String>> data);
    HashMap<String, Integer> findItemFrequencies(List<List<String>> dataset);
    HashMap<String, Integer> findItemGreaterOrEqualThreshold(List<List<String>> dataset, int threshold);
    List<List<String>> updateTransactionsAfterRemoveItem(List<List<String>> dataset, int threshold);
}
