package com.datamining.group4.dao;

import com.datamining.group4.entity.DataEntity;
import com.datamining.group4.entity.ItemSet;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Component
public class ItemsetDAO {
    private static final String SPLIT_REGEX = ",\\s*";
    public List<ItemSet> findAll(String fileName) {
        List<ItemSet> records = new ArrayList<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(SPLIT_REGEX);
                records.add(new ItemSet(Arrays.asList(values), 0));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return records;
    }
    public List<ItemSet> findFirstNItemsets(String fileName, int numOfRecords) {
        List<ItemSet> records = new ArrayList<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int i = 0;
            while((line = bufferedReader.readLine()) != null && i < numOfRecords) {
                String[] values = line.split(SPLIT_REGEX);
                records.add(new ItemSet(Arrays.asList(values), 0));
                i++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return records;
    }
}
