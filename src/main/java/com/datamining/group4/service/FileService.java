package com.datamining.group4.service;

import com.datamining.group4.entity.Itemset;

import java.util.List;

public interface FileService {
    List<Itemset> findAll(String fileName);
    List<Itemset> findFirstNItemset(String fileName, int numOfRecords);


}
