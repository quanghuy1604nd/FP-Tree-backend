package com.datamining.group4.service.impl;

import com.datamining.group4.converter.NodeConverter;
import com.datamining.group4.dto.FPTreeDTO;
import com.datamining.group4.service.PreprocessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.datamining.group4.entity.*;

import java.util.*;

@Service
public class PreprocessingServiceImpl implements PreprocessingService {
    @Autowired
    private NodeConverter nodeConverter;

    @Override
    public FPTreeDTO createTree(Node root, List<List<String>> data) {
        for(List<String> transaction : data) {
            Node p = root;
            for(String item : transaction) {
                if(p.getMapChildren().containsKey(item)) {
                    Node child = p.getMapChildren().get(item);
                    child.setSupportCount(child.getSupportCount() + 1);
                }
                else {
                    Node newChild = new Node(item, 1, p);
                    p.getMapChildren().put(item, newChild);
                    p.getChildren().add(newChild);
                }
                p = p.getMapChildren().get(item);
            }
        }

        return new FPTreeDTO(nodeConverter.toDto(root));
    }



    @Override
    public HashMap<String, Integer> findItemFrequencies(List<List<String>> dataset) {
        HashMap<String, Integer> itemFrequencies = new HashMap<>();
        for(List<String> transaction : dataset) {
            for(String item : transaction) {
                if(itemFrequencies.containsKey(item)) {
                    int count = itemFrequencies.get(item);
                    itemFrequencies.put(item, count + 1);
                }
                else {
                    itemFrequencies.put(item, 1);
                }
            }
        }
        return itemFrequencies;
    }

    @Override
    public HashMap<String, Integer> findItemGreaterOrEqualThreshold(List<List<String>> dataset, int threshold) {

        HashMap<String, Integer> itemFrequencies = findItemFrequencies(dataset),
                                 result = new HashMap<>();
        for(Map.Entry<String, Integer> entry : itemFrequencies.entrySet()) {
            if(entry.getValue() >= threshold) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    @Override
    public List<List<String>> updateTransactionsAfterRemoveItem(List<List<String>> source, int threshold) {
        HashMap<String, Integer> map = findItemGreaterOrEqualThreshold(source, threshold);
        List<List<String>> updatedDataset = new ArrayList<>();
        for(List<String> transaction : source) {
            List<String> newTransaction = new ArrayList<>();
            for(String item : transaction) {
                if(map.containsKey(item)) {
                    newTransaction.add(item);
                }
            }
            if(!newTransaction.isEmpty()) {
                newTransaction.sort(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        int cnt1 = map.get(o1), cnt2 = map.get(o2);
                        if(cnt1 == cnt2)
                            return o1.compareTo(o2);
                        return map.get(o2) - map.get(o1);
                    }
                });
                updatedDataset.add(newTransaction);
            }
        }
        return updatedDataset;
    }
}
