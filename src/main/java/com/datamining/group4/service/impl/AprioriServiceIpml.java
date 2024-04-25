package com.datamining.group4.service.impl;

import com.datamining.group4.dto.FrequentItemSetDTO;
import com.datamining.group4.dto.ItemSetDTO;
import com.datamining.group4.entity.ItemSet;
import com.datamining.group4.service.IAprioriService;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AprioriService implements IAprioriService {
    private Boolean checkCanAddToCandidate = true;
    @Override
    public FrequentItemSetDTO generateFrequentItemSets(List<ItemSet> transactions, double minsup, HashMap<String, Integer> supportOfOneItem) {
        List<List<String>> prevFrequentItemSet = new ArrayList<>();
        FrequentItemSetDTO frequentItemSetDTO = new FrequentItemSetDTO();
        minsup = Math.ceil(minsup * transactions.size());
        // lấy ra tập frequent itemSet có 1 phần tử
        List<ItemSetDTO> frequentItemSet = new ArrayList<>();
        for(String str : supportOfOneItem.keySet()) {
            if(supportOfOneItem.get(str) >= minsup) {
                List<String> itemSet = new ArrayList<>();
                itemSet.add(str);
                ItemSetDTO itemSetDTO = new ItemSetDTO(itemSet, supportOfOneItem.get(str));
                frequentItemSet.add(itemSetDTO);
                prevFrequentItemSet.add(itemSet);
            }
        }
        frequentItemSetDTO.setFrequentItemSet(frequentItemSet);
        frequentItemSetDTO = run(prevFrequentItemSet, transactions, frequentItemSetDTO, minsup);
        return  frequentItemSetDTO;
    }

    private FrequentItemSetDTO run(List<List<String>> prevFrequentItemSet, List<ItemSet> transactions, FrequentItemSetDTO frequentItemSetDTO, double minsup) {
        int k = 2;
        while (true) {
            HashMap<List<String>, Integer> supportCount = new HashMap<>();
            // lấy ra tập frequent itemSet có k phần tử
            List<List<String>> candidates = generateFrequentItemSetsWithKItem(k, prevFrequentItemSet);
            prevFrequentItemSet.clear();
            if(candidates.size() <= 0) {
                return frequentItemSetDTO;
            }
            // đếm support count của từng itemSet trong tập frequent itemSet tìm đươc
            for(List<String> itemSet : candidates) {
               for(ItemSet transaction : transactions) {
                   if(checkContainsInTransaction(transaction.getItemset(), itemSet)) {
                       supportCount.put(itemSet, supportCount.getOrDefault(itemSet, 0) + 1);
                   }
               }
            }

            List<ItemSetDTO> frequentItemSet = frequentItemSetDTO.getFrequentItemSet();
            // kiểm tra xem tập itemset nào thỏa mãn có supportCount >= minsup không
            for(List<String> itemSet : supportCount.keySet()) {
                if(supportCount.get(itemSet) >= minsup) {
                    ItemSetDTO itemSetDTO = new ItemSetDTO(itemSet, supportCount.get(itemSet));
                    frequentItemSet.add(itemSetDTO);
                    prevFrequentItemSet.add(itemSet);
                }
            }
            frequentItemSetDTO.setFrequentItemSet(frequentItemSet);
            k++;
        }
    }
    // kiểm tra xem tập itemSet có tồn tại trong tập transaction không
    private boolean checkContainsInTransaction(List<String> transaction, List<String> currentItemSet) {
        return transaction.containsAll(currentItemSet);
    }

    private List<List<String>> generateFrequentItemSetsWithKItem(int k, List<List<String>> prevFrequentItemSet) {
        int size = prevFrequentItemSet.size();
        List<List<String>> candidates = new ArrayList<>();
        for(int i = 0; i < size - 1; i++) {
            List<String> itemSet1 = prevFrequentItemSet.get(i);
            for(int j = i+1; j < size; j++) {
                List<String> itemSet2 = prevFrequentItemSet.get(j);
                boolean canJoin = true;
                for(int t = 0; t < k-2; t++) {
                    if(!itemSet1.get(t).equals(itemSet2.get(t))) {
                        canJoin = false;
                        break;
                    }
                }
                // TH thỏa mãn điều kiện k-2 phần tử đầu tiên của 2 tập trùng nhau
                if(canJoin) {
                    List<String> tmp = new ArrayList<>(itemSet1);
                    tmp.add(itemSet2.get(itemSet2.size() - 1));
                    // reduce number itemSet in candidate
                    checkCanAddToCandidate = true;
                    solveCheckReduceCandidate(tmp, prevFrequentItemSet);
                    if(checkCanAddToCandidate) {
                        candidates.add(tmp);
                    }
                }
            }

        }
        return candidates;
    }

    // kiểm tra xem có tập con nào của tập itemSet có k phần tử không tồn tại trong tập k-1 phần tử
    public void solveCheckReduceCandidate(List<String> candidate, List<List<String>> prevFrequentItemSet) {
        int k = 1;
        while (k <= candidate.size() - 1) {
            Try(1, k, candidate.size(), new ArrayList<>(), candidate, prevFrequentItemSet);
            k++;
        }
    }
    private void Try(int h, int k, int n, List<String> tmp, List<String> src, List<List<String>> prevFrequentItemSet) {
        if(tmp.size() == k) {
            // TH tập con của tập itemset không tồn tại trong tập frequent itemSet có k-1 phần tử
            if(!checkContains(prevFrequentItemSet, tmp)) {
                checkCanAddToCandidate = false;
            }
            return;
        }
        for(int i = h; i <= n; i++) {
            tmp.add(src.get(i-1));
            Try(h+1, k, n, tmp, src, prevFrequentItemSet);
            tmp.remove(tmp.size() - 1);
        }
    }
    private boolean checkContains(List<List<String>> prevFrequentItemSet, List<String> currentItemSet) {
        for(List<String> itemSet : prevFrequentItemSet) {
            if(itemSet.containsAll(currentItemSet)) {
                return true;
            }
        }
        return false;
    }
}
