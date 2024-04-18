package com.datamining.group4.service;

import com.datamining.group4.dto.RuleDTO;
import com.datamining.group4.entity.Itemset;
import com.datamining.group4.entity.Rule;

import java.util.List;

public interface RuleService {
    boolean checkRule(Rule rule);
    List<Rule> generateRuleOfItemSet(Itemset itemSet, List<Itemset> itemsetList, int itemSup, double minConf);
    List<RuleDTO> generateAllRules(List<Itemset> frequentItemSet, List<Itemset> dataset, double minConf);
}
