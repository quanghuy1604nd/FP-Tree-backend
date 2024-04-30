package com.datamining.group4.service.impl;

import com.datamining.group4.dto.FrequentItemSetDTO;
import com.datamining.group4.dto.ItemSetDTO;
import com.datamining.group4.entity.ItemSet;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AprioriServiceImpl implements com.datamining.group4.service.AprioriService {
    private Boolean checkCanAddToCandidate = true;
    private List<List<String>> prevFrequentItemSet = null;
    private FrequentItemSetDTO frequentItemSetDTO = null;
    @Override
    public FrequentItemSetDTO generateFrequentItemSets(List<ItemSet> transactions, double minsup, HashMap<String, Integer> supportOfOneItem) {
        // khởi tạo
        prevFrequentItemSet = new ArrayList<>();
        frequentItemSetDTO = new FrequentItemSetDTO();
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
        run(transactions, minsup);
        return frequentItemSetDTO;
    }

    private void run(List<ItemSet> transactions, double minsup) {
        int k = 2;
        while (true) {
            HashMap<List<String>, Integer> supportCount = new HashMap<>();
            // lấy ra tập frequent itemSet có k phần tử
            List<List<String>> candidates = generateFrequentItemSetsWithKItem(k);
            prevFrequentItemSet.clear();
            if(candidates.size() <= 0) {
                return;
            }
            // đếm support count của từng itemSet trong tập frequent itemSet tìm đươc
            for(List<String> itemSet : candidates) { // itemSet có k phần tử
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
//                    Collections.sort(itemSet);
                    prevFrequentItemSet.add(itemSet);
                }
            }
            frequentItemSetDTO.setFrequentItemSet(frequentItemSet);
            k++;
        }
    }
    // kiểm tra xem tập itemSet có tồn tại trong tập transaction không
    private boolean checkContainsInTransaction(List<String> transaction, List<String> currentItemSet) {
        for(String item : currentItemSet) {
            if(!transaction.contains(item)) {
                return false;
            }
        }
        return true;
    }

    private List<List<String>> generateFrequentItemSetsWithKItem(int k) {
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
                    solveCheckReduceCandidate(tmp);
                    if(checkCanAddToCandidate) {
                        candidates.add(tmp);
                    }
                }
            }
        }
        return candidates;
    }

    // kiểm tra xem có tập con nào của tập itemSet có k phần tử không tồn tại trong tập k-1 phần tử
    public void solveCheckReduceCandidate(List<String> candidate) {
        int k = candidate.size();
        HashMap<String, Integer> test = new HashMap<>();
        for(String item : candidate) {
            test.put(item, 1);
        }
        for (String item : candidate) {
            test.remove(item);
            if(!checkContains(new ArrayList<>(test.keySet()))) {
                checkCanAddToCandidate = false;
                return;
            }
            test.put(item, 1);
        }
    }
    // kiểm tra tập itemSet có tồn tại trong tập Frequent itemSet có k - 1 phần tử không
    private boolean checkContains(List<String> currentItemSet) {
        for(List<String> itemSet : prevFrequentItemSet) {
            if (checkContainsInTransaction(itemSet, currentItemSet)) {
                return true;
            }
        }
        return false;
//        return prevFrequentItemSet.contains(currentItemSet);
    }
    private void Try(int h, int k, int n, List<String> tmp, List<String> src) {
        Stack<Integer> stack = new Stack<>();
        stack.push(h);
        while(!stack.isEmpty()) {
            int current = stack.peek();
            if(tmp.size() == k) {
                if(!checkContains(tmp)) {
                    checkCanAddToCandidate = false;
                    return;
                }
                stack.pop();
                if(!stack.isEmpty()) {
                    int prev = stack.pop();
                    stack.push(prev + 1);
                    tmp.remove(tmp.size() - 1);
                }
                continue;
            }
            if(current > n) {
                stack.pop();
                if(!stack.isEmpty()) {
                    int prev = stack.pop();
                    stack.push(prev + 1);
                    tmp.remove(tmp.size() - 1);
                }
                continue;
            }
            tmp.add(src.get(current - 1));
            stack.push(current + 1);
        }
    }

}
