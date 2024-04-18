package com.datamining.group4.service.impl;

import com.datamining.group4.converter.FPTreeConverter;
import com.datamining.group4.converter.ItemsetConverter;
import com.datamining.group4.converter.RuleConverter;
import com.datamining.group4.dto.FPTreeDTO;
import com.datamining.group4.dto.FrequentItemsetDTO;
import com.datamining.group4.dto.ItemsetDTO;
import com.datamining.group4.dto.RuleDTO;
import com.datamining.group4.entity.*;
import com.datamining.group4.service.FPTreeService;
import com.datamining.group4.service.ItemSetService;
import com.datamining.group4.service.NodeService;
import com.datamining.group4.service.PreprocessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FPTreeServiceImpl implements FPTreeService {
    @Autowired
    private FPTreeConverter fpTreeConverter;
    @Autowired
    private ItemsetConverter itemsetConverter;
    @Autowired
    private RuleConverter ruleConverter;
    @Autowired
    private PreprocessingService preprocessingService;
    @Autowired
    private ItemSetService itemSetService;


    @Override
    public FPTreeDTO convertTree(FPTree tree) {
        return fpTreeConverter.toDto(tree);
    }

    @Override
    public void constructTree(FPTree fpTree, List<Itemset> dataset, List<Integer> frequencies) {
        List<Itemset> itemsetList = preprocessingService.updateTransactionsAfterRemoveItem(dataset, frequencies, fpTree.getThreshold());
        fpTree.createTree(itemsetList, frequencies);
    }


    private int sumSupportCountOfItem(FPTree fpTree, String item) {
        Node p = fpTree.getHeaderTable().get(item);
        int sum = 0;
        while(p != null) {
            sum += p.getSupportCount();
            p = p.getLink();
        }

        return sum;
    }
    @Override
    public void mineTree(FPTree fpTree, Set<String> prefix, List<Itemset> frequentItemList) {
        List<String> itemList = new ArrayList<>(fpTree.getHeaderTable().keySet().stream().toList());
        itemList.sort((o1, o2) -> {
            int num1 = fpTree.getHeaderTable().get(o1).getSupportCount(), num2 = fpTree.getHeaderTable().get(o2).getSupportCount();
            if (num1 == num2) {
                return o2.compareTo(o1);
            }
            return num1 - num2;
        });

        for(String item : itemList) {
            Set<String> newFreSet = new HashSet<>(prefix);
            newFreSet.add(item);
            int sumSupportOfItemInTree = this.sumSupportCountOfItem(fpTree, item);

            frequentItemList.add(new Itemset(newFreSet.stream().toList(), sumSupportOfItemInTree));
            Pair<List<Itemset>, List<Integer>> prefixPaths = itemSetService.findPrefixPathsOfItem(fpTree, item);
            List<Itemset> conditionalPatterns = prefixPaths.getKey();
            List<Integer> frequencies = prefixPaths.getValue();
            FPTree conditionalTree = new FPTree(fpTree.getMinSup(), fpTree.getSizeOfTransactions());
            constructTree(conditionalTree, conditionalPatterns, frequencies);

            if(!conditionalTree.getHeaderTable().isEmpty()) {
                mineTree(conditionalTree, newFreSet, frequentItemList);
            }
        }
    }

}
