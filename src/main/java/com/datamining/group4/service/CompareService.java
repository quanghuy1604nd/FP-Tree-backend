package com.datamining.group4.service;

import com.datamining.group4.dto.CompareResponseDTO;
import com.datamining.group4.entity.ItemSet;

import java.util.List;

public interface CompareService {
    CompareResponseDTO compare(List<ItemSet> dataset);
}
