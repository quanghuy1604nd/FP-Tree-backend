package com.datamining.group4.service;

import com.datamining.group4.dto.RuleDTO;
import com.datamining.group4.entity.ItemSet;
import com.datamining.group4.entity.Rule;

import java.util.List;

public interface RuleService {
    boolean checkRule(Rule rule);
    List<Rule> generateRuleOfItemSet(ItemSet itemSet, List<ItemSet> itemSetList, int itemSup, double minConf);
    List<RuleDTO> generateAllRules(List<ItemSet> frequentItemSet, List<ItemSet> dataset, double minConf);
}
