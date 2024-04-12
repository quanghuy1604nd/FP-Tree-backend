package com.datamining.group4.converter;

import com.datamining.group4.dto.FPTreeDTO;
import com.datamining.group4.dto.NodeDTO;
import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class FPTreeConverter {
    @Autowired
    private NodeConverter nodeConverter;

    public FPTreeDTO toDto(FPTree entity) {
        FPTreeDTO dto = new FPTreeDTO();
        dto.setNodeDTO(nodeConverter.toDto(entity.getRoot()));
        LinkedHashMap <String, NodeDTO> headerTable = new LinkedHashMap<>();
        for(Map.Entry<String, Node> x : entity.getHeaderTable().entrySet()) {
            headerTable.put(x.getKey(), nodeConverter.toDto(x.getValue()));
        }
        dto.setHeaderTable(headerTable);
        return dto;
    }
}
