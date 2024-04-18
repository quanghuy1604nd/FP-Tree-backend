package com.datamining.group4.service.impl;

import com.datamining.group4.converter.RuleConverter;
import com.datamining.group4.dto.RuleDTO;
import com.datamining.group4.entity.Itemset;
import com.datamining.group4.entity.Pair;
import com.datamining.group4.entity.Rule;
import com.datamining.group4.service.ItemSetService;
import com.datamining.group4.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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


    private Rule createRule(StringBuilder pattern, Itemset itemset, int support) {
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
        return new Rule(new Itemset(antecedence, 0), new Itemset(consequence, 0), support, -1);
    }
    private List<Rule> generateRulesOfFrequentItemSet(Itemset itemset, List<Itemset> itemsetList, double minConf) {
        List<Rule> res = new ArrayList<>();
        int n = itemset.getItemset().size();
        Queue<Pair<StringBuilder, Integer>> q = new LinkedList<>();
        StringBuilder s = new StringBuilder("0".repeat(n));
        q.add(new Pair<>(s, n));
        boolean flag = true; // cờ cho lần đầu duyệt loai bỏ 0000
        while(!q.isEmpty()) {
            int sz = q.size();
            for(int j = 0; j < sz; j++) {
                Pair<StringBuilder, Integer> top = q.poll();

                // Chú ý chỗ này sẽ là support count không phải support
                // nghĩa là không chia cho fpt.getSizeOfTransactions()

                Rule rule = createRule(top.getKey(), itemset, (int) itemset.getSupport());
                if(flag || checkRule(rule, itemsetList, (int) itemset.getSupport(), minConf)) {
                    if(!flag) res.add(rule);
                    // thêm những nút con
                    for(int k = top.getValue() - 1; k >= 0; k--) {
                        StringBuilder x = new StringBuilder(top.getKey());
                        if(x.charAt(k) != '1') {
                            x.setCharAt(k, '1');
                            q.add(new Pair<>(x, k));
                        }
                    }
                }

            }
            flag = false;
        }

        return res;
    }

    private boolean checkRule(Rule rule, List<Itemset> itemsetList, int itemSup, double minConf) {
        if(rule.getAntecedence().getItemset().isEmpty() || rule.getConsequence().getItemset().isEmpty())
            return false;
        int antecedenceSup = itemSetService.getSupport(rule.getAntecedence(), itemsetList);
        double conf = itemSup * 1.0 / antecedenceSup;
        rule.setConfident(conf);
        System.out.println(rule);
        System.out.println(antecedenceSup + " " + conf);
        return conf >= minConf;
    }
    @Override
    public List<Rule> generateRuleOfItemSet(Itemset itemSet, List<Itemset> itemsetList, int itemSup, double minConf) {
        return null;
    }

    @Override
    public List<RuleDTO> generateAllRules(List<Itemset> frequentItemSets, List<Itemset> dataset, double minConf) {
        List<Rule> rules = new ArrayList<>();
        for(Itemset itemset : frequentItemSets) {
            System.out.println(itemset.getSupport());
            rules.addAll(this.generateRulesOfFrequentItemSet(itemset, dataset, minConf));
        }
        return rules.stream().map(ruleConverter::toDto).toList();
    }
}
