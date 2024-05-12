package com.datamining.group4.service.impl;

import com.datamining.group4.configuration.StorageProperties;
import com.datamining.group4.dto.FrequentItemSetAndRuleDTO;
import com.datamining.group4.dto.FrequentItemSetDTO;
import com.datamining.group4.dto.MetaFile;
import com.datamining.group4.exception.StorageException;
import com.datamining.group4.service.StorageService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
@Service
public class StorageServiceImpl implements StorageService {
    private final Path path;
    @Autowired
    private Environment environment;

    @Autowired
    public StorageServiceImpl(StorageProperties properties) {
        this.path = Paths.get(properties.getLocation());
    }

    @Override
    public String createUniqueFileName() {
        return new SimpleDateFormat("yyyyMMddHHmmssSS").format(new Date()) + ".csv";
    }

    @Override
    public MetaFile storeInputFile(MultipartFile file, double minSup, double minConf) {
        try {
            if(file.isEmpty()) {
                throw new StorageException("Cannot be empty");
            }
            String originalFileName = file.getOriginalFilename();
            String storedName = createUniqueFileName();
            MetaFile metaFile = new MetaFile(originalFileName, storedName, minSup, minConf);
            FileOutputStream fos = new FileOutputStream(getPathToInputFile(storedName));
            fos.write(file.getBytes());
            return metaFile;

        }
        catch (IOException e) {
            throw new StorageException("Cannot save file!", e);
        }
    }

    @Override
    public String getPathToDirectoryStoreInputFile(String fileName) {
        String[] arr = fileName.split("\\.");
        StringBuilder folderName = new StringBuilder();
        for(int i = 0; i < arr.length - 1; i++) {
            folderName.append(arr[i]);
        }
        File newDir = new File(String.valueOf(Paths.get(this.path.toString(), folderName.toString())));
        if (!newDir.exists()){
            newDir.mkdirs();
        }
        return path.toString() + '/' + folderName;
    }

    @Override
    public String getPathToInputFile(String fileName) {
        return getPathToDirectoryStoreInputFile(fileName) +'/' + fileName;
    }

    @Override
    public void storeFrequentItemSetsAndRuleFPGrowth(String fileName, FrequentItemSetAndRuleDTO frequentItemSetAndRuleDTO, double minSup, double minConf) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try(FileWriter fileWriter = new FileWriter(getPathToDirectoryStoreInputFile(fileName) + "/FPG_frequentItemSetsAndRules_minSup_" + minSup + "_minConf_" + minConf + ".json")) {
            gson.toJson(frequentItemSetAndRuleDTO, fileWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
