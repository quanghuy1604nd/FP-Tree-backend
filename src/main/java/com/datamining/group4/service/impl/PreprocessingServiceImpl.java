package com.datamining.group4.service.impl;

import com.datamining.group4.service.PreprocessingService;
import org.springframework.stereotype.Service;
import com.datamining.group4.entity.*;

import java.util.*;

@Service
public class PreprocessingServiceImpl implements PreprocessingService {

    @Override
    public HashMap<String, Integer> findItemFrequencies(List<ItemSet> dataset, List<Integer> frequencies) {
        HashMap<String, Integer> itemFrequencies = new HashMap<>();
        for (int i = 0; i < dataset.size(); i++) {
            List<String> itemset = dataset.get(i).getItemset();
            for (String item : itemset) {
                if (itemFrequencies.containsKey(item)) {
                    int count = itemFrequencies.get(item);
                    itemFrequencies.put(item, count + frequencies.get(i));
                } else {
                    itemFrequencies.put(item, frequencies.get(i));
                }
            }
        }
        return itemFrequencies;
    }

    @Override
    public HashMap<String, Integer> findItemGreaterOrEqualThreshold(List<ItemSet> dataset, List<Integer> frequencies, int threshold) {
        HashMap<String, Integer> itemFrequencies = findItemFrequencies(dataset, frequencies),
                result = new HashMap<>();
        for (Map.Entry<String, Integer> entry : itemFrequencies.entrySet()) {
            if (entry.getValue() >= threshold) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    @Override
    public List<ItemSet> updateTransactionsAfterRemoveItem(List<ItemSet> source, List<Integer> frequencies, int threshold) {
        HashMap<String, Integer> map = findItemGreaterOrEqualThreshold(source, frequencies, threshold);
        List<ItemSet> updatedDataset = new ArrayList<>();
        for (int i = 0; i < source.size(); i++) {
            ItemSet itemset = source.get(i);
            List<String> newTransaction = new ArrayList<>();
            for (String item : itemset.getItemset()) {
                if (map.containsKey(item)) {
                    newTransaction.add(item);
                }
            }
            newTransaction.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    int cnt1 = map.get(o1), cnt2 = map.get(o2);
                    if (cnt1 == cnt2)
                        return o1.compareTo(o2);
                    return cnt2 - cnt1;
                }
            });
            updatedDataset.add(new ItemSet(newTransaction, frequencies.get(i)));
        }
        return updatedDataset;
    }
}
