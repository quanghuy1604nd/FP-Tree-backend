package com.datamining.group4.converter;

import com.datamining.group4.dto.ItemSetDTO;
import com.datamining.group4.entity.ItemSet;
import org.springframework.stereotype.Component;

@Component
public class ItemSetConverter {
    public ItemSetDTO toDto(ItemSet entity) {
        ItemSetDTO dto = new ItemSetDTO();
        dto.setItemset(entity.getItemset());
        dto.setSupport(entity.getSupport());
        return dto;
    }
    public ItemSet toEntity(ItemSetDTO dto) {
        ItemSet entity = new ItemSet();
        entity.setItemset(dto.getItemset());
        entity.setSupport(dto.getSupport());
        return entity;
    }
}
