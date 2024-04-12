package com.datamining.group4.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Node {
    private String itemName;
    private int supportCount;
    private Node parent;
    private List<Node> children;
    private LinkedHashMap<String, Node> mapChildren;
    private Node link;
    public Node(String itemName, int supportCount, Node parent) {
        this.itemName = itemName;
        this.supportCount = supportCount;
        this.parent = parent;
        this.mapChildren = new LinkedHashMap<>();
        this.children = new ArrayList<>();
    }
}
