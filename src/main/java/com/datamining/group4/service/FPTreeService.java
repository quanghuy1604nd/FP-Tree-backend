package com.datamining.group4.service;

import com.datamining.group4.entity.FPTree;

import java.util.List;

public interface FPTreeService {
    List<List<String>> generateFrequentItemsets(FPTree fpTree);

}
