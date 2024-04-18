package com.datamining.group4.service;

import com.datamining.group4.entity.Node;

import java.util.List;

public interface NodeService {
    List<String> asendFpTree(Node node, String item);
}
