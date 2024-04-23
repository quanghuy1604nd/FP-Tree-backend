package com.datamining.group4.service.impl;

import com.datamining.group4.dao.ItemsetDAO;
import com.datamining.group4.entity.DataEntity;
import com.datamining.group4.entity.ItemSet;
import com.datamining.group4.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private ItemsetDAO itemsetDAO;

    @Override
    public List<ItemSet> findAll(String fileName) {
        return itemsetDAO.findAll(fileName);
    }
    @Override
    public List<ItemSet> findFirstNItemset(String fileName, int numOfRecords) {
        return itemsetDAO.findFirstNItemsets(fileName, numOfRecords);
    }

    @Override
    public DataEntity findAllTransactions(String fileName) {
        return itemsetDAO.findAllTransactionAndSupport(fileName);
    }

}
