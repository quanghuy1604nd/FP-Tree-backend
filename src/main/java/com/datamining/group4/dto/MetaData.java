package com.datamining.group4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetaData {
    private Integer numOfTransactions;
    private Integer numOfItems;
    private List<ItemSetDTO> transactions;
    private HashMap<String, Integer> itemFrequencies;
}
