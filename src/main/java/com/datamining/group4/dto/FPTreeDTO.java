package com.datamining.group4.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FPTreeDTO {
    private NodeDTO nodeDTO;
    @JsonIgnore
    private LinkedHashMap<String, NodeDTO> headerTable;
}
