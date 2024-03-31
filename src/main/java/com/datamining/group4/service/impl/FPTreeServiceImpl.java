package com.datamining.group4.service.impl;

import com.datamining.group4.converter.NodeConverter;
import com.datamining.group4.dto.NodeDTO;
import com.datamining.group4.service.FPTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.datamining.group4.entity.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class FPTreeServiceImpl implements FPTreeService {
    @Autowired
    private NodeConverter nodeConverter;
    private static final String SPLIT_REGEX = ",";

    private NodeDTO convertTree(Node entity) {
        NodeDTO dto = nodeConverter.toDto(entity);
        List<NodeDTO> list = new ArrayList<>();
        for(Node node : entity.getChildren()) {
            list.add(convertTree(node));
        }
        dto.setChildren(list);
        return dto;
    }

    @Override
    public NodeDTO createTree(Node root, List<List<String>> data) {
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

        return convertTree(root);
    }

    @Override
    public List<List<String>> readCsv(String fileName) {
        List<List<String>> records = new ArrayList<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(SPLIT_REGEX);
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return records;
    }

    @Override
    public HashMap<String, Integer> findItemGreaterOrEqualThreshold(List<List<String>> dataset, int threshold) {
        HashMap<String, Integer> itemFrequency = new HashMap<>();
        for(List<String> transaction : dataset) {
            for(String item : transaction) {
                if(itemFrequency.containsKey(item)) {
                    int count = itemFrequency.get(item);
                    itemFrequency.put(item, count + 1);
                }
                else {
                    itemFrequency.put(item, 1);
                }
            }
        }
        HashMap<String, Integer> result = new HashMap<>();
        for(Map.Entry<String, Integer>entry : itemFrequency.entrySet()) {
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
