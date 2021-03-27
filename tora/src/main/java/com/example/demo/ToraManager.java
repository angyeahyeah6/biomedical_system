package com.example.demo;

import com.google.common.collect.Multiset;
import ken.evaluation.IndexScore;
import ken.network.PredicateNetwork;
import ken.node.LiteratureNode;
import ken.util.Utils;
import weka.core.Instances;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ToraManager {
    Map<String, Map<String, IndexScore>> perfectRank;
//    List<String> allDrugs;
    private static Logger logger = Logger.getLogger("LearningModel");
    public static void main(String[] args) {
        String drug = "Cobalt";
        ToraManager manager = new ToraManager();
        Integer cnt = 0;
        List<IndexScore> diseaseRank = manager.predictRank(drug);
        Map<String, Map<String, Object>> evaluationScore = new HashMap<>();
        for(IndexScore score : diseaseRank){
            Map<String, Object> tmp = new HashMap<>();
            tmp.put("feature1", score.getFeature());
            tmp.put("feature2", score.getFeature2());
            tmp.put("id", cnt.toString());
            cnt += 1;
            evaluationScore.put(score.getName(),tmp);
        }
//        return evaluationScore;
//        String drug = "Keratin-19";
//        Map<String, Instances> instMap = (Map<String, Instances>)
//                Utils.readObjectFile("dat_file/preInstMap/" + drug + ".dat");
//        Integer displayCount = 20;
//        ToraManager manager = new ToraManager();
//        // c ranking
//        List<IndexScore> diseaseRank = manager.predictRank(drug);
//        // find b through drug (a)
//        Map<String, Map<String, Integer>> drugPredicateNeighborCount = manager.findPredicateNeighborsCount(drug);
//        Map<String, Map<String, Map<String, Integer>>> IntermediatePredicatwithDisease = new HashMap<>();
//
//        for(IndexScore score : diseaseRank.subList(0, displayCount)){
////            instMap.get(score.getName())
//            // find b through c
//            Map<String, Map<String, Integer>> tmp = manager.findPredicateNeighborsCount(score.getName());
//            // find the union neighbors
//            Set<String> neighbors = tmp.keySet().stream().collect(Collectors.toSet());
//            Set<String> neighborsOfDrug = drugPredicateNeighborCount.keySet().stream().collect(Collectors.toSet());
//            neighbors.retainAll(neighborsOfDrug);
//
//
//
//            // b -> c -> predicate -> integer
//            for(String b : neighbors){
//                Map<String, Map<String, Integer>> c_predicate = new HashMap<>();
//                c_predicate.put(score.getName(), tmp.get(b));
//                if(IntermediatePredicatwithDisease.get(b) == null){
//                    IntermediatePredicatwithDisease.put(b, c_predicate);
//                }
//                else{
//                    Map<String, Map<String, Integer>> prev_c = IntermediatePredicatwithDisease.get(b);
//                    prev_c.put(score.getName(), tmp.get(b));
//                    IntermediatePredicatwithDisease.replace(b, prev_c);
//                }
//
//            }
//            List<String> removeList = new ArrayList<>();
//            for(Map.Entry<String, Map<String, Map<String, Integer>>> b_c : IntermediatePredicatwithDisease.entrySet()){
//                if(b_c.getValue().keySet().size() > displayCount/3){
//                    removeList.add(b_c.getKey());
//                }
//            }
//            for(String rm : removeList){
//                IntermediatePredicatwithDisease.remove(rm);
//            }
////
//////            IntermediatePredicatwithDisease.put(score.getName(),
//////                    tmp.entrySet().stream().filter(x -> neighbors.contains(x.getKey().toString())).collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue())));
//        }

    }

    public ToraManager() {
        String rankPath = "dat_file/pre/goldenRank.dat";
        perfectRank = (Map<String, Map<String, IndexScore>>) Utils.readObjectFile(rankPath);
//        allDrugs = Utils.readLineFile("vocabulary/allDrugs_seed.txt");
    }


    /*
        predict rank
        return : a drug's ranking disease with two feature value
        data structure: List<IndexScore>
     */
    public List<IndexScore> predictRank(String drug) {
        Map<String, List<String>> ndcgMap = new HashMap<>();
        List<String> drugs = new ArrayList<>(perfectRank.keySet());
        List<IndexScore> perfectScoreLs = new ArrayList<>();
        List<IndexScore> evalScoreLs = new ArrayList<>();
        int diseaseC = 0;
        try {
            Map<String, IndexScore> perfectDrugRank = perfectRank.get(drug);
            Map<String, Instances> instMap = (Map<String, Instances>)
                    Utils.readObjectFile("dat_file/preInstMap/" + drug + ".dat");
            for (String disease : perfectDrugRank.keySet()) {
                PredicateNetwork predicateNet = new PredicateNetwork(drug, disease, instMap.get(disease));
                //TODO setting feature type

                predicateNet.setOnlyStatInsts();
//                predicateNet.setOnlyContentInsts();
//                predicateNet.setOnlyConcept();
//                predicateNet.setOnlyPredicationInsts();
//                predicateNet.setFeatureSelectionInsts();
                predicateNet.predictInterm();
                IndexScore item = new IndexScore(disease);
                IndexScore perfectItem = perfectDrugRank.get(disease);
                //TODO setting ranking method

//                item.setFeature(predicateNet.getExpdectProb());
                item.setFeature(predicateNet.getImportCount(), predicateNet.getExpdectProb());
                item.setRealScore(perfectItem.getRealScore());
                evalScoreLs.add(item);
                perfectScoreLs.add(perfectItem);
            }
            Collections.sort(evalScoreLs, Collections.reverseOrder(new IndexScore.FeatureComparator()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return evalScoreLs;
    }

    /*
        predict rank
        return : a drug's all neighbor predicate counts
        data structure: Map<String, Map<String, Integer>>
     */

    public static Map<String, Map<String, Integer>> findPredicateNeighborsCount(String subject){
        Map<String, Map<String, Integer>> predicateMap = new HashMap<>();
        LiteratureNode drugNode = new LiteratureNode(subject, 1809, 2004);
//
        // predicateNeighborPost return subject node's all neighbors nodes and the all predicate type between them
        Map<String, Multiset<String>> predicateNeighborPost = drugNode.getPredicateNeighbors();

        Map<String, Map<String, Integer>> returnObject = new HashMap<>();
        for(Map.Entry<String, Multiset<String>> drugB : predicateNeighborPost.entrySet()){
            Map<String, Integer> predicateCount = new HashMap<>();
            for (String unqPredicate : drugB.getValue().elementSet()){
                predicateCount.put(unqPredicate, drugB.getValue().count(unqPredicate));
            }
            returnObject.put(drugB.getKey(), predicateCount);
        }
        return returnObject;
    }

    /*
        find union neighbor between drug and list of disease (find B)
        return : union neighbor
        data structure: List<String>
     */
    public static  List<String> findUnionNeighbors (Set<String> drugNeighbors, List<String> diseaseNeighbors) {
        HashSet<String> unionNeighbors = new HashSet<String>(drugNeighbors);
        unionNeighbors.addAll(diseaseNeighbors);
        return unionNeighbors.stream().collect(Collectors.toList());
    }
    // TODO: // find the union neighbors between subject and targetC

}
