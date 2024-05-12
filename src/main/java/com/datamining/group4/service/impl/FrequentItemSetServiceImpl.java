package com.datamining.group4.service.impl;

import com.datamining.group4.converter.ItemSetConverter;
import com.datamining.group4.dto.FrequentItemSetDTO;
import com.datamining.group4.dto.ItemSetDTO;
import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.ItemSet;
import com.datamining.group4.service.FPTreeService;
import com.datamining.group4.service.FrequentItemSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
@Service
public class FrequentItemSetServiceImpl implements FrequentItemSetService {
    @Autowired
    private FPTreeService fpTreeService;
    @Autowired
    private ItemSetConverter itemsetConverter;
    @Override
    public FrequentItemSetDTO generateFrequentItemSets(FPTree fpTree) {
        List<ItemSet> frequentItemList = new ArrayList<>();
        fpTreeService.mineTree(fpTree, new HashSet<>(), frequentItemList);
        List<ItemSetDTO> updatedFrequentItemSet = frequentItemList.stream().map(itemsetConverter::toDto).toList();
//        List<ItemSetDTO> updatedFrequentItemSet = frequentItemList.stream().peek(x -> x.setSupport(x.getSupport() / fpTree.getSizeOfTransactions()))
//                .map(itemsetConverter::toDto).toList();
        return new FrequentItemSetDTO(updatedFrequentItemSet);
    }

}
