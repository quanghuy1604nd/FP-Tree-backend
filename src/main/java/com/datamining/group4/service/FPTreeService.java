package com.datamining.group4.service;

import com.datamining.group4.dto.NodeDTO;
import com.datamining.group4.entity.Node;

import java.util.HashMap;
import java.util.List;

public interface FPTreeService {
    NodeDTO createTree(Node root, List<List<String>> data);
    List<List<String>> readCsv(String fileName);
    HashMap<String, Integer> findItemGreaterOrEqualThreshold(List<List<String>> dataset, int threshold);
    List<List<String>> updateTransactionsAfterRemoveItem(List<List<String>> source, int threshold);
}
