package com.datamining.group4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompareResponseDTO {
    private MeasureDTO duration;
    private MeasureDTO memory;
}
