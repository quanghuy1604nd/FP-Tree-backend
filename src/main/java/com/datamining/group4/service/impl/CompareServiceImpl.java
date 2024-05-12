package com.datamining.group4.service.impl;

import com.datamining.group4.dto.CompareResponseDTO;
import com.datamining.group4.dto.FrequentItemSetDTO;
import com.datamining.group4.dto.MeasureDTO;
import com.datamining.group4.entity.FPTree;
import com.datamining.group4.entity.ItemSet;
import com.datamining.group4.entity.Node;
import com.datamining.group4.entity.Pair;
import com.datamining.group4.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class CompareServiceImpl implements CompareService {
    @Autowired
    private FPTreeService fpTreeService;
    @Autowired
    private FrequentItemSetService frequentItemSetService;
    @Autowired
    private AprioriService aprioriService;
    @Autowired
    private PreprocessingService preprocessingService;

    private Pair<Long, Long> getMeasureOfMinSupOfApriori(List<ItemSet> dataset, double minSup) {
        System.gc();
        Runtime rt = Runtime.getRuntime();
        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long start = System.currentTimeMillis();
        List<Integer> frequencies = Collections.nCopies(dataset.size(), 1);
        HashMap<String, Integer> frequenciesOfPreItem = preprocessingService.findItemFrequencies(dataset, frequencies);
        FrequentItemSetDTO frequentItemSets = aprioriService.generateFrequentItemSets(dataset, minSup, frequenciesOfPreItem);
        long duration = System.currentTimeMillis() - start;
        long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        return new Pair<>(duration, (afterUsedMem-beforeUsedMem) / 1024);
    }

    private Pair<Long, Long> getMeasureOfMinSupOfFPGrowth(List<ItemSet> dataset, double minSup) {
        System.gc();
        List<Integer> frequencies = Collections.nCopies(dataset.size(), 1);
        Runtime rt = Runtime.getRuntime();
        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();

        long start = System.currentTimeMillis();
        Node rootEntity = new Node("root", 0, null);
        LinkedHashMap<String, Node> headerTableEntity = new LinkedHashMap<>();
        FPTree fpTree = new FPTree(rootEntity, headerTableEntity, minSup, dataset.size());
        fpTreeService.constructTree(fpTree, dataset, frequencies);
        FrequentItemSetDTO frequentItemSets = frequentItemSetService.generateFrequentItemSets(fpTree);
        long duration = System.currentTimeMillis() - start;
        long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        frequentItemSets.setDuration(duration);
//        System.out.println(prevFree + "," + free);
        return new Pair<>(duration,( afterUsedMem-beforeUsedMem) / 1024);
    }
    @Override
    public CompareResponseDTO compare(List<ItemSet> dataset) {
        MeasureDTO durations = new MeasureDTO();
        MeasureDTO memories = new MeasureDTO();
        for(double minSup = 0.01; minSup <= 0.5; minSup += 0.02) {
            Pair<Long, Long> FPTGrowthRes = this.getMeasureOfMinSupOfFPGrowth(dataset, minSup);
            Pair<Long, Long> AprioriRes = this.getMeasureOfMinSupOfApriori(dataset, minSup);
            durations.getFpTree().add(FPTGrowthRes.getKey());
            memories.getFpTree().add(FPTGrowthRes.getValue());
            durations.getApriori().add(AprioriRes.getKey());
            memories.getApriori().add(AprioriRes.getValue());
        }
        return new CompareResponseDTO(durations, memories);
    }
}
