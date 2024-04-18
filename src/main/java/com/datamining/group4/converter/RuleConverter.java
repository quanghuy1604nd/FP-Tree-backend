package com.datamining.group4.converter;

import com.datamining.group4.dto.RuleDTO;
import com.datamining.group4.entity.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RuleConverter {
    @Autowired
    private ItemsetConverter itemsetConverter;
    public RuleDTO toDto(Rule entity) {
        RuleDTO dto = new RuleDTO();
        dto.setAntecedence(itemsetConverter.toDto(entity.getAntecedence()));
        dto.setConsequence(itemsetConverter.toDto(entity.getConsequence()));
        dto.setSupport(entity.getSupport());
        dto.setConfident(entity.getConfident());
        return dto;
    }
}
