package com.datamining.group4.service.impl;

import com.datamining.group4.entity.Node;
import com.datamining.group4.service.NodeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class NodeServiceImpl implements NodeService {
    @Override
    public List<String> asendFpTree(Node node, String item) {
        List<String> prefixPath = new ArrayList<>();
        Node leaf = node;
        while(leaf.getParent() != null) {
            prefixPath.add(leaf.getItemName());
            leaf = leaf.getParent();
        }
        return prefixPath.subList(1, prefixPath.size());
    }
}
