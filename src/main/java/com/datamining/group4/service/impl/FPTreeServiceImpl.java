package com.datamining.group4.service.impl;

import com.datamining.group4.converter.FPTreeConverter;
import com.datamining.group4.dto.FPTreeDTO;
import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.Node;
import com.datamining.group4.entity.Pair;
import com.datamining.group4.service.FPTreeService;
import com.datamining.group4.service.PreprocessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FPTreeServiceImpl implements FPTreeService {
    @Autowired
    private FPTreeConverter fpTreeConverter;
    @Autowired
    private PreprocessingService preprocessingService;
    @Override
    public FPTreeDTO convertTree(FPTree tree) {
        return fpTreeConverter.toDto(tree);
    }

    @Override
    public FPTree constructTree(FPTree fpTree, List<List<String>> dataset, List<Integer> frequencies) {
        List<List<String>> itemsetList = preprocessingService.updateTransactionsAfterRemoveItem(dataset, frequencies, fpTree.getMinSup());
        fpTree.createTree(itemsetList, frequencies);
        return fpTree;
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
    public Pair<List<List<String>>, List<Integer>> findPrefixPathsOfItem(FPTree fpTree, String item) {
        List<List<String>> patterns = new ArrayList<>();
        List<Integer> frequencesOfEachPattern = new ArrayList<>();
        LinkedHashMap<String, Node> headerTable = fpTree.getHeaderTable();
        Node node = headerTable.get(item);
        while(node != null) {
            List<String> prefixPath = asendFpTree(node, item);
            patterns.add(prefixPath);
            frequencesOfEachPattern.add(node.getSupportCount());

            node = node.getLink();
        }
        return new Pair<>(patterns, frequencesOfEachPattern);
    }

    @Override
    public List<List<String>> generateFrequentItemsets(FPTree fpTree) {
        List<List<String>> frequentItemList = new ArrayList<>();
        mineTree(fpTree, new ArrayList<>(), frequentItemList);
        return frequentItemList;
    }

    @Override
    public void mineTree(FPTree fpTree, List<String> prefix, List<List<String>> frequentItemList) {
        List<String> itemList = new ArrayList<>(fpTree.getHeaderTable().keySet().stream().toList());
        itemList.sort((o1, o2) -> {
            int num1 = fpTree.getHeaderTable().get(o1).getSupportCount(), num2 = fpTree.getHeaderTable().get(o2).getSupportCount();
            if (num1 == num2) {
                return o2.compareTo(o1);
            }
            return num1 - num2;
        });
        for(String item : itemList) {
            List<String> newFreSet = new ArrayList<>(prefix);
            newFreSet.add(item);
            frequentItemList.add(newFreSet);
            Pair<List<List<String>>, List<Integer>> prefixPaths = findPrefixPathsOfItem(fpTree, item);
            List<List<String>> conditionalPatterns = prefixPaths.getKey();
            List<Integer> frequencies = prefixPaths.getValue();
            FPTree conditionalTree = new FPTree(fpTree.getMinSup());
            constructTree(conditionalTree, conditionalPatterns, frequencies);
            if(conditionalTree.getHeaderTable() != null) {
                mineTree(conditionalTree, newFreSet, frequentItemList);
            }
        }
    }
}
