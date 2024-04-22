package com.datamining.group4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleDTO {
    private ItemSetDTO antecedence;
    private ItemSetDTO consequence;
    private double support;
    private double confident;
}
