package com.datamining.group4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrequentItemSetDTO {
    private List<ItemSetDTO> frequentItemSet;
    private long duration;
    public FrequentItemSetDTO(List<ItemSetDTO> frequentItemSet) {
        this.frequentItemSet = frequentItemSet;
    }
}
