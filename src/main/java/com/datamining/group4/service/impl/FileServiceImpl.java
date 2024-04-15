package com.datamining.group4.service.impl;

import com.datamining.group4.dao.ItemsetDAO;
import com.datamining.group4.entity.Itemset;
import com.datamining.group4.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private ItemsetDAO itemsetDAO;

    @Override
    public List<Itemset> findAll(String fileName) {

        return itemsetDAO.findAll(fileName);
    }
    @Override
    public List<Itemset> findFirstNItemset(String fileName, int numOfRecords) {
        return itemsetDAO.findFirstNItemsets(fileName, numOfRecords);
    }

}
