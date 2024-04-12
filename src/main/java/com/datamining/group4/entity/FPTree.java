package com.datamining.group4.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FPTree {
    private Node root;
    private LinkedHashMap<String, Node> headerTable;
    private int minSup;

    public FPTree(int minSup) {
        this.root = new Node("null", 0, null);
        this.headerTable = new LinkedHashMap<>();
        this.minSup = minSup;
    }

    public void addNodeToHeaderTable(String item, Node node) {
        if(headerTable.containsKey(item)) {
            Node p = headerTable.get(item);
            while(p.getLink() != null) {
                p = p.getLink();
            }
            p.setLink(node);
        }
        else {
            this.headerTable.put(item, node);
        }

    }
    public void createTree(List<List<String>> data, List<Integer> frequency) {
        for(int i = 0; i < data.size(); i++) {
            List<String> transaction = data.get(i);
            Node p = this.root;
            for(String item : transaction) {
                if(p.getMapChildren().containsKey(item)) {
                    Node child = p.getMapChildren().get(item);
                    child.setSupportCount(child.getSupportCount() + frequency.get(i));
                }
                else {
                    Node newChild = new Node(item, frequency.get(i), p);
                    p.getMapChildren().put(item, newChild);
                    p.getChildren().add(newChild);
                    this.addNodeToHeaderTable(item, newChild);
                }
                p = p.getMapChildren().get(item);
            }
        }
    }
    public void createConditionalPatternsBase(List<List<String>> dataset) {
        for(List<String> transaction : dataset) {
            Node p = this.root;
            for(String item : transaction) {
                if(p.getMapChildren().containsKey(item)) {
                    Node child = p.getMapChildren().get(item);
                    child.setSupportCount(child.getSupportCount() + 1);
                }
                else {
                    Node newChild = new Node(item, 1 , p);
                    p.getMapChildren().put(item, newChild);
                    p.getChildren().add(newChild);
                }
                p = p.getMapChildren().get(item);
            }
        }
    }
}
