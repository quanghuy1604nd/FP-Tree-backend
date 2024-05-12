package com.datamining.group4.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetaFile {
    private String displayName;
    private String storedName;
    private double minSup;
    private double minConf;
}
