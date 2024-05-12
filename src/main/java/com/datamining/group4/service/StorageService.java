package com.datamining.group4.service;

import com.datamining.group4.dto.FrequentItemSetAndRuleDTO;
import com.datamining.group4.dto.FrequentItemSetDTO;
import com.datamining.group4.dto.MetaFile;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String createUniqueFileName();
    MetaFile storeInputFile(MultipartFile file, double minSup, double minConf);
    String getPathToDirectoryStoreInputFile(String fileName);
    String getPathToInputFile(String fileName);
    void storeFrequentItemSetsAndRuleFPGrowth(String fileName, FrequentItemSetAndRuleDTO frequentItemSetAndRuleDTO, double minSup, double minConf);
}
