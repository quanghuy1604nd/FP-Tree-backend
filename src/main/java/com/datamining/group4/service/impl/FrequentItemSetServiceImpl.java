package com.datamining.group4.service.impl;

import com.datamining.group4.converter.ItemsetConverter;
import com.datamining.group4.dto.FrequentItemsetDTO;
import com.datamining.group4.dto.ItemsetDTO;
import com.datamining.group4.dto.RuleDTO;
import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.Itemset;
import com.datamining.group4.service.FPTreeService;
import com.datamining.group4.service.FrequentItemSetService;
import com.datamining.group4.service.RuleService;
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
    private ItemsetConverter itemsetConverter;
    @Autowired
    private RuleService ruleService;
    @Override
    public FrequentItemsetDTO generateFrequentItemSets(FPTree fpTree, List<Itemset> dataset, double minConf) {
        List<Itemset> frequentItemList = new ArrayList<>();
        long start = System.currentTimeMillis();
        fpTreeService.mineTree(fpTree, new HashSet<>(), frequentItemList);
        long duration = System.currentTimeMillis() - start;
//        List<ItemsetDTO> updatedFrequentItemset = frequentItemList.stream().peek(x -> x.setSupport(x.getSupport() / fpTree.getSizeOfTransactions())).
//                map(itemsetConverter::toDto).toList();
        List<RuleDTO> rules = ruleService.generateAllRules(frequentItemList, dataset, minConf);
        List<ItemsetDTO> updatedFrequentItemset = frequentItemList.stream().map(itemsetConverter::toDto).toList();
        return new FrequentItemsetDTO(updatedFrequentItemset, rules);
    }

}
