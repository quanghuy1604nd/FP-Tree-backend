package com.datamining.group4.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rule {
    private Itemset antecedence;
    private Itemset consequence;
    private double support;
    private double confident;
}
