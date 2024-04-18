package com.datamining.group4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrequentItemsetDTO {
    private List<ItemsetDTO> frequentItemset;
    private List<RuleDTO> rules;
    private long duration;
    public FrequentItemsetDTO(List<ItemsetDTO> frequentItemset, List<RuleDTO> rules) {
        this.frequentItemset = frequentItemset;
        this.rules = rules;
    }
}
