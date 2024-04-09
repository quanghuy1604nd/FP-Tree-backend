package com.datamining.group4.service;

import java.util.List;

public interface FileService {
    List<List<String>> readCsv(String fileName);
    List<List<String>> getFirstNRecords(String fileName, int numOfRecords);


}
