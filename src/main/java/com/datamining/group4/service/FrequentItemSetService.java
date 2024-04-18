package com.datamining.group4.service;

import com.datamining.group4.dto.FrequentItemsetDTO;
import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.Itemset;

import java.util.List;

public interface FrequentItemSetService {
    FrequentItemsetDTO generateFrequentItemSets(FPTree fpTree, List<Itemset> dataset, double minConf);
}
