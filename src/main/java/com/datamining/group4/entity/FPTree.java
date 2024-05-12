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
    private double minSup;
    private int sizeOfTransactions;

    public int getThreshold() {
        return (int) Math.ceil(this.minSup * this.sizeOfTransactions);
    }

    public FPTree(double minSup, int sizeOfTransactions) {
        this.root = new Node("null", 0, null);
        this.headerTable = new LinkedHashMap<>();
        this.minSup = minSup;
        this.sizeOfTransactions = sizeOfTransactions;
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
    public void createTree(List<ItemSet> data, List<Integer> frequency) {
        for(int i = 0; i < data.size(); i++) {
            ItemSet transaction = data.get(i);
            Node p = this.root;
            for(String item : transaction.getItemset()) {
                if(p.getMapChildren().containsKey(item)) {
                    Node child = p.getMapChildren().get(item);
                    child.setSupportCount(child.getSupportCount() + frequency.get(i));
                }
                else {
                    Node newChild = new Node(item, frequency.get(i), p);
                    p.getMapChildren().put(item, newChild);
//                    p.getChildren().add(newChild);
                    this.addNodeToHeaderTable(item, newChild);
                }
                p = p.getMapChildren().get(item);
            }

        }
    }
}
