package com.datamining.group4.service.impl;

import com.datamining.group4.service.FileService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class FileServiceImpl implements FileService {
    private static final String SPLIT_REGEX = ",\\s*";

    @Override
    public List<List<String>> readCsv(String fileName) {
        List<List<String>> records = new ArrayList<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(SPLIT_REGEX);
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return records;
    }
    @Override
    public List<List<String>> getFirstNRecords(String fileName, int numOfRecords) {
        List<List<String>> records = new ArrayList<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int i = 0;
            while((line = bufferedReader.readLine()) != null && i < numOfRecords) {
                String[] values = line.split(SPLIT_REGEX);
                records.add(Arrays.asList(values));
                i++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return records;
    }

}
