package com.datamining.group4.service;

import com.datamining.group4.dto.FrequentItemSetDTO;
import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.ItemSet;

import java.util.List;

public interface FrequentItemSetService {
    FrequentItemSetDTO generateFrequentItemSets(FPTree fpTree);
}
