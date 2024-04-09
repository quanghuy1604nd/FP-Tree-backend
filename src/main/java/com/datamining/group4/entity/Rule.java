package com.datamining.group4.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rule {
    private List<String> antecedence;
    private List<String> consequence;
    private double support;
    private double confident;
}
