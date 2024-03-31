package com.datamining.group4.service.impl;

import com.datamining.group4.configuration.StorageProperties;
import com.datamining.group4.entity.CSVFile;
import com.datamining.group4.exception.StorageException;
import com.datamining.group4.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
@Service
public class StorageServiceImpl implements StorageService {
    private final Path path;

    @Autowired
    public StorageServiceImpl(StorageProperties properties) {
        this.path = Paths.get(properties.getLocation());
    }

    @Override
    public String createUniqueFileName() {
        return new SimpleDateFormat("yyyyMMddHHmmssSS").format(new Date()) + ".csv";
    }

    @Override
    public CSVFile storeFile(MultipartFile file) {
        try {
            if(file.isEmpty()) {
                throw new StorageException("Cannot be empty");
            }
            String originalFileName = file.getOriginalFilename();
            String storedName = createUniqueFileName();
            CSVFile csvFile = new CSVFile(originalFileName, storedName);
            FileOutputStream fos = new FileOutputStream(getPathToFile(storedName));
            fos.write(file.getBytes());
            return csvFile;

        }
        catch (IOException e) {
            throw new StorageException("Cannot save file!", e);
        }
    }

    @Override
    public String getPathToFile(String fileName) {
        return path.toString() +'/'+ fileName;
    }
}
