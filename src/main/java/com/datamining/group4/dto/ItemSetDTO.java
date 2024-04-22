package com.datamining.group4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemSetDTO {
    private List<String> itemset;
    private double support;
}
