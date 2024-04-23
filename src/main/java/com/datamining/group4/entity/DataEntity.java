package com.datamining.group4.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataEntity {
    private List<ItemSet> listItemSet;
    private HashMap<String, Integer> supportCount = new HashMap<>();
}
