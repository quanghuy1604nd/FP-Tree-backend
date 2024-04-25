package com.datamining.group4.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor

public class MeasureDTO {
    private List<Long> fpTree;
    private List<Long> apriori;

    public MeasureDTO() {
        this.fpTree = new ArrayList<>();
        this.apriori = new ArrayList<>();
    }
}
