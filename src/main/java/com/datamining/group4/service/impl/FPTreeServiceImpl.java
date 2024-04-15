package com.datamining.group4.service.impl;

import com.datamining.group4.converter.FPTreeConverter;
import com.datamining.group4.converter.ItemsetConverter;
import com.datamining.group4.dto.FPTreeDTO;
import com.datamining.group4.dto.FrequentItemsetDTO;
import com.datamining.group4.dto.ItemsetDTO;
import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.Itemset;
import com.datamining.group4.entity.Node;
import com.datamining.group4.entity.Pair;
import com.datamining.group4.service.FPTreeService;
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
    private PreprocessingService preprocessingService;
    @Override
    public FPTreeDTO convertTree(FPTree tree) {
        return fpTreeConverter.toDto(tree);
    }

    @Override
    public void constructTree(FPTree fpTree, List<Itemset> dataset, List<Integer> frequencies) {
        List<Itemset> itemsetList = preprocessingService.updateTransactionsAfterRemoveItem(dataset, frequencies, fpTree.getThreshold());
        fpTree.createTree(itemsetList, frequencies);
    }

    @Override
    public List<String> asendFpTree(Node node, String item) {
        List<String> prefixPath = new ArrayList<>();
        Node leaf = node;
        while(leaf.getParent() != null) {
            prefixPath.add(leaf.getItemName());
            leaf = leaf.getParent();
        }
        return prefixPath.subList(1, prefixPath.size());
    }

    @Override
    public Pair<List<Itemset>, List<Integer>> findPrefixPathsOfItem(FPTree fpTree, String item) {
        List<Itemset> patterns = new ArrayList<>();
        List<Integer> frequenciesOfEachPattern = new ArrayList<>();
        LinkedHashMap<String, Node> headerTable = fpTree.getHeaderTable();
        Node node = headerTable.get(item);
        while(node != null) {
            List<String> prefixPath = asendFpTree(node, item);
            if(!prefixPath.isEmpty()) {
                patterns.add(new Itemset(prefixPath, node.getSupportCount()));
                frequenciesOfEachPattern.add(node.getSupportCount());
            }

            node = node.getLink();
        }
        return new Pair<>(patterns, frequenciesOfEachPattern);
    }

    @Override
    public FrequentItemsetDTO generateFrequentItemsets(FPTree fpTree) {
        List<Itemset> frequentItemList = new ArrayList<>();
        long start = System.currentTimeMillis();
        mineTree(fpTree, new HashSet<>(), frequentItemList);
        long duration = System.currentTimeMillis() - start;
        List<ItemsetDTO> updatedFrequentItemset = frequentItemList.stream().peek(x -> x.setSupport(x.getSupport() / fpTree.getSizeOfTransactions())).
                map(itemsetConverter::toDto).toList();
//        List<ItemsetDTO> updatedFrequentItemset = frequentItemList.stream().map(itemsetConverter::toDto).toList();

        return new FrequentItemsetDTO(updatedFrequentItemset, duration);
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
            Pair<List<Itemset>, List<Integer>> prefixPaths = findPrefixPathsOfItem(fpTree, item);
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
