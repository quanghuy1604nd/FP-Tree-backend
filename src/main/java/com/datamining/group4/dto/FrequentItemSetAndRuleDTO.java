package com.datamining.group4.dto;

import com.datamining.group4.entity.FrequentItemSet;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrequentItemSetAndRuleDTO {
    @JsonIgnore
    private FrequentItemSetDTO frequentItemSet;
    private List<RuleDTO> rules;
    private long duration;
    public FrequentItemSetAndRuleDTO(FrequentItemSetDTO frequentItemSet, List<RuleDTO> rules) {
        this.frequentItemSet = frequentItemSet;
        this.rules = rules;
    }
}
