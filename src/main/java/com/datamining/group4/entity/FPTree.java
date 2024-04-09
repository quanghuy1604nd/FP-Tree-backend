package com.datamining.group4.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FPTree {
    private Node root;
    private HashMap<String, ArrayList<Node>> HeaderTable;
    private int minSup;
}
