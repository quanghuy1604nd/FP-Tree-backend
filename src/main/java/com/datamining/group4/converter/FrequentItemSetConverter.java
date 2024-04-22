package com.datamining.group4.converter;

import com.datamining.group4.dto.FrequentItemSetDTO;
import com.datamining.group4.entity.FrequentItemSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FrequentItemSetConverter {
    @Autowired
    private ItemSetConverter itemsetConverter;
    public FrequentItemSetDTO toDto(FrequentItemSet entity) {
        FrequentItemSetDTO dto = new FrequentItemSetDTO();
        dto.setFrequentItemSet(entity.getFrequentItemSet().stream().map(itemsetConverter::toDto).toList());
        return dto;
    }
    public FrequentItemSet toEntity(FrequentItemSetDTO dto) {
        FrequentItemSet entity = new FrequentItemSet();
        entity.setFrequentItemSet(dto.getFrequentItemSet().stream().map(itemsetConverter::toEntity).toList());
        return entity;
    }
}
