package com.datamining.group4.service;

import com.datamining.group4.entity.ItemSet;

import java.util.HashMap;
import java.util.List;

public interface PreprocessingService {
    HashMap<String, Integer> findItemFrequencies(List<ItemSet> dataset, List<Integer> frequencies);
    HashMap<String, Integer> findItemGreaterOrEqualThreshold(List<ItemSet> dataset, List<Integer> frequencies, int threshold);
    List<ItemSet> updateTransactionsAfterRemoveItem(List<ItemSet> dataset, List<Integer> frequencies, int threshold);
}
