package com.datamining.group4.dto;

import lombok.*;

import java.util.List;

;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeDTO {
    private String name;
    private Attributes attributes;
    private List<NodeDTO> children;
}
