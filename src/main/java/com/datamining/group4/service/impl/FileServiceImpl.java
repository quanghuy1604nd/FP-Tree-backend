package com.datamining.group4.service.impl;

import com.datamining.group4.dao.DAO;
import com.datamining.group4.entity.ItemSet;
import com.datamining.group4.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private DAO DAO;

    @Override
    public List<ItemSet> findAll(String fileName) {
        return DAO.findAll(fileName);

    }
    @Override
    public List<ItemSet> findFirstNItemset(String fileName, int numOfRecords) {
        return DAO.findFirstNItemsets(fileName, numOfRecords);
    }

}
