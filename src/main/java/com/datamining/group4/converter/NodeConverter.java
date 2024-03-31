package com.datamining.group4.converter;

import com.datamining.group4.dto.Attributes;
import com.datamining.group4.dto.NodeDTO;
import com.datamining.group4.entity.Node;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class NodeConverter {
    public Node toEntity(NodeDTO dto) {
        Node entity = new Node();
        entity.setItemName(dto.getName());
        entity.setChildren(dto.getChildren().stream().map(this::toEntity).toList());

        return entity;
    }
    public NodeDTO toDto(Node entity) {
        NodeDTO dto = new NodeDTO();
        dto.setName(entity.getItemName());
        Attributes att = new Attributes(entity.getSupportCount());
        dto.setAttributes(att);
        dto.setChildren(entity.getChildren().stream().map(this::toDto).toList());
        return dto;
    }
}
