package com.datamining.group4.service;

import com.datamining.group4.entity.DataEntity;
import com.datamining.group4.entity.ItemSet;

import java.util.List;

public interface FileService {
    List<ItemSet> findAll(String fileName);
    List<ItemSet> findFirstNItemset(String fileName, int numOfRecords);

}
