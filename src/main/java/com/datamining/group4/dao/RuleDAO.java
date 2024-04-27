package com.datamining.group4.dao;

import com.datamining.group4.dto.RuleDTO;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RuleDAO {
    public void storeInCSVFile(List<RuleDTO> rules) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("products.csv"), StandardCharsets.UTF_8));
            for (RuleDTO ruleDTO : rules) {
                StringBuilder line = new StringBuilder();

                bw.write(line.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e){}
    }
}
