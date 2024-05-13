package com.datamining.group4.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemSet {
    private List<String> itemset;
    private double support;
}
