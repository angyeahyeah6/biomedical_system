# biomedical_system

#### 環境說明: Windows 10, java 64bit 1.8.0_131

#### IDE: intellij idea 2017.1

#### Library manager: maven

#### 程式執行說明:

1. 處理 SemMedDB 資料
   - Download from https://skr3.nlm.nih.gov/SemMed/ (DB schema 說明請看官網介紹)
   - 執行 ken.prepare.SemMedPreparser 內 function 產生 DB biomedical_literature_by_year 內 table (之後用到的 features 都從幾張表而來，我已經處理好放在 James 上了，可以直接沿用):
     - create_mesh_predication_aggregate() => mesh_predication_aggregate
     - processMedlineGroupByYear() => mesh_concept_by_year
     - create_neighbor_by_predication() => neighbor_by_predication
     - processCooccurNeighborGroupByYear() => neighbor_cooccur
2. 評估 Path Importance Classification
   - 執行 ken.label.AttrTester.main()，詳細資訊請看程式註解
     1. 執行 createStatArffFile()、createContentArffFile()、mergeInst()產生 3 個 arff 檔案 (stat.arff, content.arff, combined.arff)
     2. 可以使用 weka 保留不同 feature type( predication-based, concept-based)，並存成新檔案，如下圖。之後使用可以 evaluateModel()評估新 data set
     3. 利用前面步驟產生的檔案(input)執行 evaluateModel()，選擇 model (請看 TODO 範例)進行評估
   - 檔案說明 4. output/attr/內的檔案: weka 定義的檔案，請使用文字編輯器打開 or 使用 weka 軟體打開 5. vocabulary/61predicate.txt: SemMedDB 內全部的 predicate，下 SQL 而來: SELECT distinct(predicate) FROM semmed_ver26.predication_aggregate 6. vocabulary/contentWord.txt: 篩選後的 predicate，濾掉出現頻率不多的 predicate，如何過濾請看 paper 7. vocabulary/trainingContentWord.txt: 與 contentWord.txt 內容相同但順序不同，因 weka 處理後會變這個順序，之後評估 Drug Repurposing Discovery 所需要的 content arff 要和 training cases 一致，不然會有問題
3. 評估 Drug Repurposing Discovery
   - 前處理: 執行 ken.prepare.InstancePrepare.main()，詳細資訊請看程式註解 (若使用我留下的檔案，則不需要重跑，有些 function 要跑蠻久的)
     1. 執行 createGoldenFile()產生所有 drug 標準答案，default time cut-off year = 2005，若要修改請自行修改程式，輸出檔案 dat_file/eval/golden_pairRank.dat
     2. 執行 createTreatRankSet()，input 為前一步驟產生的 golden_pairRank.dat，保留標準答案有 3(TREATS)的藥物與初步過濾(有分數的/全部>=0.01)，輸出檔案 dat_file/eval/golden_treatRank.dat
     3. 執行 create500Evalrank()，input 為 golden_treatRank.dat，將分數包成物件，僅保留 focal drug(隨機挑選 500 個 drug)的標準答案，輸出檔案 dat_file/eval/goldenRank.dat 與 vocabulary/500drugs_seed.txt (藥名)
     4. 執行 createInstFile()計算 focal drug-disease pair 的變數，input 為 dat_file/eval/goldenRank.dat 與 vocabulary/500drugs_seed.txt ，輸出檔案 dat_file/instaMap/xxxx (focal drug).dat
     5. Used for benchmark 2: 執行 createCooccurFile()，input 為 dat_file/eval/goldenRank.dat"，輸出檔案為 dat_file/cooccur/xxxx (focal drug).dat
   - 計算 NDCG: 執行 ken.model.LearningModel.main()即可，詳細資訊請看程式註解。執行程式前須確定前處理都已經完成
     1. 執行 runModel()、baseLine()、baseLine2()產生 ndcg 檔案，input 為 dat_file/instaMap/xxxx (focal drug).dat (runModel() & baseLine())與 dat_file/cooccur/xxxx (focal drug).dat (baseLine2())，需自行設置 feature type, classifier, ranking method, 輸出檔名(請看 TODO)，輸出檔案為 ndcg object
     2. 執行 ken.evaluation. Evaluator.main()得到比較結果(ex: 提升 3.4%)，input 有兩個，為前一步驟得到的 ndcg object (benchmark and 自己的 model)，輸出檔案為所有 disease 平均 ndcg score，輸出檔案 output/ndcg/xxxx.txt(xxxx 與輸入檔案相同)
   - 其他檔案說明(基本上不會改動)
     1. vocabulary/500drugs_seed.txt: focal drug 檔案，create from ken.prepare.InstancePrepare.create500Evalrank()
     2. vocabulary/drug.txt, disease.txt, genes_proteins_enzyme.txt, anatomy.txt, pathology.txt: 全部的 seed concept, 共 17264 個
   - 需要手動設置的程式 (如何設定請看 TODO)
     1. ken.model.LearningModel.runModel()
     2. ken.network.PredicateNetwork.setClassifier()
     3. ken.evaluation.main()

#### DB 說明:

- DB 名稱: biomedical_concept_mapping (主要是儲存不同 vocabulary terms 之間的對應關係，請看學姊程式，我沿用並沒有修改)
  - mesh_descriptor、mesh_supplement: 原始 mesh 檔案只取出特定欄位(Created from: sandy.parser.MeshParser)
  - mesh_umls: mesh term 對應的 umls concept (Producedd from: sandy.parser.MeshMappingParser)
  - omim_mesh: omim 名稱和 mesh term 的對應 (Created from: sandy.parser.MeshMappingParser)
  - omim_meshid: omim ID 和 mesh ID 的對應 (Created from: sandy.parser.CtdParser)
  - omim_name: 完整的 omim 名稱表，用於 ID 轉換 (Created from: sandy.parser.OmimParser)
  - umls_mapped_term: umls 原始資料讀入 (Created from: sandy.parser.UmlsParser)
  - umls_mesh: umls concept 對應的 mesh term (Created from: sandy.parser.MeshMappingParser)
- DB 名稱: biomedical_relation (請看學姊程式，我沿用並沒有修改)
  - drugbank: DrugBank 讀入的原始 relation (Created from: sandy.parser.DrugBankParser)
  - View: drugbank_mesh_target
  - SQL: CREATE VIEW `drugbank_mesh_target` AS select `biomedical_concept_mapping`.`drugbank_mesh`.`ID` AS `DBID`, `biomedical_concept_mapping`.`drugbank_mesh`.`Name` AS `DBName`, `biomedical_concept_mapping`.`drugbank_mesh`.`Mesh` AS `Mesh`,`biomedical_relation`.`drugbank`.`TargetName` AS `TargetName`, `biomedical_relation`.`drugbank`.`TargetSwissProtID` AS `TargetUniprotID` from (`biomedical_concept_mapping`.`drugbank_mesh` join `biomedical_relation`.`drugbank`on(((`biomedical_concept_mapping`.`drugbank_mesh`.`ID` = `biomedical_relation`.`drugbank`.`ID`) and (`biomedical_concept_mapping`.`drugbank_mesh`.`Name` = `biomedical_relation`.`drugbank`.`Name`))))
  - omim_relation: omim 讀入的原始 relation (Created from: sandy.parser.OmimParser)
  - View: omim_mesh_relation
  - SQL: CREATE VIEW `omim_mesh_relation` AS select `biomedical_concept_mapping`.`omim_mesh`.`MIM` AS `MIM`, `biomedical_concept_mapping`.`omim_mesh`.`Name` AS `Name`, `biomedical_concept_mapping`.`omim_mesh`.`Mesh` AS `Mesh`,`biomedical_relation`.`omim_relation`.`Related_MIM` AS `Related_MIM` from (`biomedical_concept_mapping`.`omim_mesh` join `biomedical_relation`.`omim_relation` on(((`biomedical_concept_mapping`.`omim_mesh`.`MIM` =`biomedical_relation`.`omim_relation`.`MIM`) and (`biomedical_concept_mapping`.`omim_mesh`.`Name` = `biomedical_relation`.`omim_relation`.`Name`))))
  - ctd_relation: ctd 讀入的原始 relation (Created from: sandy.parser.CtdParser)
  - ctd_neighbor: 轉換 ctd_relation 的儲存格式 (Created from: sandy.parser.CtdParser)
- DB 名稱: biomedical_literature_by_year
  - mesh_concept_by_year: 儲存 concept 根據年份共在 predication 出現幾次(freq)與 document frequency:
    - Created from: ken.prepare.SemMedPreparser.processMedlineGroupByYear()
  - mesh_predication_aggregate: 將 semmed_ver26.predicationaggregate 裡的 umls concept 轉成 mesh term，並只保留所需的欄位:
    - Created from: ken.prepare.SemMedPreparser.create_mesh_predication_aggregate()
  - mesh_seeds: 17264 個 concept seeds, 與 vocabulary/那五個檔案相同，儲存其他表所用的 mesh_id
  - neighbor_by_predication: 處理 semmed_ver26.predicationaggregate，並將共同出現的 concept 儲存起來，同時記錄共同出現的年份與次數:
    - Created from ken.prepare.SemMedPreparser.create_neighbor_by_predication()
  - neighbor_cooccur: 與 neighbor_by\* predication 相似，不過 based on co-occurrence method (參考 paper):
    - Created from ken.prepare.SemMedPreparser.processCooccurNeighborGroupByYear()


# yawi_biomedical_system_frontend

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Run your tests
```
npm run test
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).

### Recommendation
[EChart](https://echarts.apache.org/zh/index.html).
[Element UI](https://element.eleme.io/#/zh-CN/component/layout).

