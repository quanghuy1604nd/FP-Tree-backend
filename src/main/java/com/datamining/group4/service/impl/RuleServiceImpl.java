package com.datamining.group4.service.impl;

import com.datamining.group4.converter.RuleConverter;
import com.datamining.group4.dto.RuleDTO;
import com.datamining.group4.entity.ItemSet;
import com.datamining.group4.entity.Pair;
import com.datamining.group4.entity.Rule;
import com.datamining.group4.service.ItemSetService;
import com.datamining.group4.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RuleServiceImpl implements RuleService {
    @Autowired
    private ItemSetService itemSetService;
    @Autowired
    private RuleConverter ruleConverter;
    @Override
    public boolean checkRule(Rule rule) {
        return false;
    }


    private Rule createRule(StringBuilder pattern, ItemSet itemset, int support) {
        List<String> antecedence = new ArrayList<>();
        List<String> consequence = new ArrayList<>();

        for(int i = 0; i < pattern.length(); i++) {
            if(pattern.charAt(i) == '0') {
                antecedence.add(itemset.getItemset().get(i));
            }
            else {
                consequence.add(itemset.getItemset().get(i));
            }
        }
        return new Rule(new ItemSet(antecedence, 0), new ItemSet(consequence, 0), support, -1);
    }
    private List<Rule> generateRulesOfFrequentItemSet(ItemSet itemset, List<ItemSet> itemSetList, double minConf) {
        List<Rule> res = new ArrayList<>();
        int n = itemset.getItemset().size();
        Queue<Pair<StringBuilder, Integer>> q = new LinkedList<>();
        StringBuilder s = new StringBuilder("0".repeat(n));
        boolean init = true;
        q.add(new Pair<>(s, n));
        while(!q.isEmpty()) {
            int sz = q.size();
            Map<String, Pair<Integer, Boolean>> mark = new LinkedHashMap<>();
            for(int j = 0; j < sz; j++) {
                Pair<StringBuilder, Integer> top = q.poll();

                // Chú ý chỗ này sẽ là support count không phải support
                // nghĩa là không chia cho fpt.getSizeOfTransactions()

                Rule rule = createRule(top.getKey(), itemset, (int) itemset.getSupport());
                boolean isValidRule = checkRule(rule, itemSetList, (int) itemset.getSupport(), minConf);
                if(init || isValidRule) {
                    if(!init) res.add(rule);
                    // thêm những nút con
                    for(int k = top.getValue() - 1; k >= 0; k--) {
                        StringBuilder x = new StringBuilder(top.getKey());
                        if(x.charAt(k) != '1') {
                            x.setCharAt(k, '1');
                            mark.put(x.toString(), new Pair<>(k, true));
                        }
                    }
                }
                else if(!isValidRule){
                    for(int k = top.getValue() + 1; k < n; k++) {
                        StringBuilder x = new StringBuilder(top.getKey());
                        if(x.charAt(k) != '1') {
                            x.setCharAt(k, '1');
                            mark.put(x.toString(), new Pair<>(k, false));
                        }
                    }
                }
            }
            for(Map.Entry<String, Pair<Integer, Boolean>> x : mark.entrySet()) {
                StringBuilder key = new StringBuilder(x.getKey());
                int k = x.getValue().getKey();
                boolean flag = x.getValue().getValue();
//                System.out.println(key + " " + k + " " + flag);
                if(flag) {
                    q.add(new Pair<>(key, k));
                }
            }
            init = false;
        }

        return res;
    }

    private boolean checkRule(Rule rule, List<ItemSet> itemSetList, int itemSup, double minConf) {
        if(rule.getAntecedence().getItemset().isEmpty() || rule.getConsequence().getItemset().isEmpty())
            return false;
        int antecedenceSup = itemSetService.getSupport(rule.getAntecedence(), itemSetList);
//        System.out.println(rule);
//
//        System.out.println(antecedenceSup + " " + itemSup);
        double conf = itemSup * 1.0 / antecedenceSup;
        rule.setConfident(conf);
//        System.out.println(antecedenceSup + " " + conf);
        return conf >= minConf;
    }
    @Override
    public List<Rule> generateRuleOfItemSet(ItemSet itemSet, List<ItemSet> itemSetList, int itemSup, double minConf) {
        return null;
    }

    @Override
    public List<RuleDTO> generateAllRules(List<ItemSet> frequentItemSets, List<ItemSet> dataset, double minConf) {
        List<Rule> rules = new ArrayList<>();
        for(ItemSet itemset : frequentItemSets) {
//            System.out.println(itemset.getSupport());
            rules.addAll(this.generateRulesOfFrequentItemSet(itemset, dataset, minConf));
        }
        return rules.stream().map(ruleConverter::toDto).toList();
    }
}
