package com.datamining.group4.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Metadata {
    private String displayName;
    private String storedName;
    private double minSup;

}
