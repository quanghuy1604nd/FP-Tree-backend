package com.datamining.group4.service.impl;

import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.ItemSet;
import com.datamining.group4.entity.Node;
import com.datamining.group4.entity.Pair;
import com.datamining.group4.service.ItemSetService;
import com.datamining.group4.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
@Service
public class ItemSetServiceImpl implements ItemSetService {
    @Autowired
    private NodeService nodeService;
    @Override
    public int getSupport(ItemSet itemset, List<ItemSet> itemSetList) {
        if(itemset.getItemset().isEmpty()) return 0;
        int cnt = 0;
        for(ItemSet x : itemSetList) {
            if(new HashSet<>(x.getItemset()).containsAll(itemset.getItemset())) {
                cnt++;
            }
        }
        return cnt;
    }

    @Override
    public Pair<List<ItemSet>, List<Integer>> findPrefixPathsOfItem(FPTree fpTree, String item) {
        List<ItemSet> patterns = new ArrayList<>();
        List<Integer> frequenciesOfEachPattern = new ArrayList<>();
        LinkedHashMap<String, Node> headerTable = fpTree.getHeaderTable();
        Node node = headerTable.get(item);
        while(node != null) {
            List<String> prefixPath = nodeService.asendFpTree(node, item);
            if(!prefixPath.isEmpty()) {
                patterns.add(new ItemSet(prefixPath, node.getSupportCount()));
                frequenciesOfEachPattern.add(node.getSupportCount());
            }

            node = node.getLink();
        }
        return new Pair<>(patterns, frequenciesOfEachPattern);
    }
}
