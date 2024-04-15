package com.datamining.group4.converter;

import com.datamining.group4.dto.ItemsetDTO;
import com.datamining.group4.entity.Itemset;
import org.springframework.stereotype.Component;

@Component
public class ItemsetConverter {
    public ItemsetDTO toDto(Itemset entity) {
        ItemsetDTO dto = new ItemsetDTO();
        dto.setItemset(entity.getItemset());
        dto.setSupport(entity.getSupport());
        return dto;
    }
    public Itemset toEntity(ItemsetDTO dto) {
        Itemset entity = new Itemset();
        entity.setItemset(dto.getItemset());
        entity.setSupport(dto.getSupport());
        return entity;
    }
}
