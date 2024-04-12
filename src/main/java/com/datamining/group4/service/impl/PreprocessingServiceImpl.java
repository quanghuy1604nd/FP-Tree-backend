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

    @Override
    public HashMap<String, Integer> findItemFrequencies(List<List<String>> dataset, List<Integer> frequencies) {
        HashMap<String, Integer> itemFrequencies = new HashMap<>();
        for(int i = 0; i < dataset.size(); i++) {
            List<String> transaction = dataset.get(i);
            for(String item : transaction) {
                if(itemFrequencies.containsKey(item)) {
                    int count = itemFrequencies.get(item);
                    itemFrequencies.put(item, count + frequencies.get(i));
                }
                else {
                    itemFrequencies.put(item, frequencies.get(i));
                }
            }
        }
        return itemFrequencies;
    }

    @Override
    public HashMap<String, Integer> findItemGreaterOrEqualThreshold(List<List<String>> dataset, List<Integer> frequencies, int threshold) {

        HashMap<String, Integer> itemFrequencies = findItemFrequencies(dataset, frequencies),
                                 result = new HashMap<>();
        for(Map.Entry<String, Integer> entry : itemFrequencies.entrySet()) {
            if(entry.getValue() >= threshold) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    @Override
    public List<List<String>> updateTransactionsAfterRemoveItem(List<List<String>> source, List<Integer> frequencies, int threshold) {
        HashMap<String, Integer> map = findItemGreaterOrEqualThreshold(source, frequencies, threshold);
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
