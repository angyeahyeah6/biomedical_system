package com.example.demo;


import com.example.demo.inputClass.GetEval;
import com.example.demo.inputClass.GetPredicate;
import ken.evaluation.IndexScore;
import ken.util.Utils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin("http://localhost:8082")
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ToraController {
    @PostMapping("/get_predicate")
    public Map getPredictRank(@RequestBody GetPredicate getPredicate) {
        String drug = getPredicate.drugName;
        ToraManager manager = new ToraManager();
        List<IndexScore> diseaseRank = manager.predictRank(drug);
        Map<String, Map<String, Integer>> drugPredicateNeighborCount = manager.findPredicateNeighborsCount(drug);
        Map<String, Map<String, Map<String, Integer>>> IntermediatePredicatwithDisease = new HashMap<>();
        for(IndexScore score : diseaseRank.subList(0, 20)){
            Map<String, Map<String, Integer>> tmp = manager.findPredicateNeighborsCount(score.getName());
            // find the union neighbors
            Set<String> neighbors = tmp.keySet().stream().collect(Collectors.toSet());
            Set<String> neighborsOfDrug = drugPredicateNeighborCount.keySet().stream().collect(Collectors.toSet());
            neighbors.retainAll(neighborsOfDrug);
            IntermediatePredicatwithDisease.put(score.getName(),
                    tmp.entrySet().stream().filter(x -> neighbors.contains(x.getKey().toString())).collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue())));
        }
        return IntermediatePredicatwithDisease;
    }
    @GetMapping("/all_drug")
    public List<String> getAllDrug() {
        List<String> drugsAll = Utils.readLineFile("vocabulary/allDrugs_seed.txt");
        return drugsAll;
    }
    @PostMapping("/get_eval")
    public Map<String, Map<String, Double>> getRankingDetail(@RequestBody GetEval getEval) {
        String drug = "Cobalt";
        ToraManager manager = new ToraManager();
        List<IndexScore> diseaseRank = manager.predictRank(drug);
        Map<String, Map<String, Double>> evaluationScore = new HashMap<>();
        for(IndexScore score : diseaseRank){
            Map<String, Double> tmp = new HashMap<>();
            tmp.put("feature1", score.getFeature());
            tmp.put("feature2", score.getFeature2());
            evaluationScore.put(score.getName(),tmp);
        }
        return evaluationScore;

    }
//    @PostMapping("/test")
//    public String test(@RequestBody GetPredicate test) {
//        return test.drugName;
//    }
}

