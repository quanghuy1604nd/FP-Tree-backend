package com.datamining.group4.service;

import com.datamining.group4.dto.FrequentItemSetDTO;
import com.datamining.group4.entity.ItemSet;

import java.util.HashMap;
import java.util.List;

public interface AprioriService {
    FrequentItemSetDTO generateFrequentItemSets(List<ItemSet> transactions, double minsup, HashMap<String, Integer> supportOfOneItem);
}
