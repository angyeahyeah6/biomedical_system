<template>
  <div v-loading="loading">
    <!-- title -->
    <div style="height: 250px; background-color: #fedc3d; display: flex; align-items: center">
      <div style="width: 100%; display: flex; justify-content: center">
        <div
          style="
            width: 928px;
            height: 90px;
            background-color: #01abaa;
            border-radius: 30px;
            display: flex;
            align-items: center;
            justify-content: center;
          "
        >
          <div style="font-style: normal; font-weight: bold; font-size: 48px; line-height: 58px; color: #fedc3d">
            Biomedical Network System
          </div>
        </div>
      </div>
    </div>
    <!-- search bar -->
    <div style="display: flex; justify-content: center">
      <div style="margin-top: 30px; display: flex; align-items: center; justify-content: space-between; width: 928px">
        <el-autocomplete
          style="width: 280px"
          v-model="drugName"
          :fetch-suggestions="queryDrug"
          placeholder="Drug Name"
          clearable
        >
        </el-autocomplete>
        <el-input style="width: 280px" v-model="year" clearable placeholder="Query Year"></el-input>
        <el-select style="width: 280px" v-model="type" placeholder="Classifier Type">
          <el-option v-for="item in classifierTypes" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </div>
    </div>
    <!-- search btn -->
    <div style="display: flex; justify-content: center; margin-top: 10px">
      <div style="width: 928px; display: flex; justify-content: flex-end">
        <el-button type="primary" plain icon="el-icon-s-promotion" @click="handleDetailChart">To Chart</el-button>
        <el-button type="primary" plain icon="el-icon-search" @click="handleSearchClick" :disabled="!readyToSearch">
          Search
        </el-button>
      </div>
    </div>

    <!-- collapse item -->
    <div style="display: flex; justify-content: center; margin-top: 30px">
      <el-collapse style="width: 928px" v-model="activeCollapse">
        <el-collapse-item class="collapse-item-header" name="biomedicalGraph">
          <template slot="title">
            <span class="collapse-title">Biomedical Detail</span>
          </template>
          <net :predication="predication" :drugName="drugName" :active.sync="activeGraph"></net>
        </el-collapse-item>

        <el-collapse-item class="collapse-item-header" name="detailEvaluation">
          <template slot="title">
            <span class="collapse-title">Content Detail</span>
          </template>
          <evaluation
            :evaluation="evaluation"
            :active.sync="activeEval"
            :drugName="drugName"
            :year="year"
            :type="type"
            :addDetail.sync="addDetail"
          ></evaluation>
        </el-collapse-item>
        <el-collapse-item class="collapse-item-header" name="detailNet">
          <template slot="title">
            <span class="collapse-title">Detail Net</span>
          </template>
          <detail :sendFlag="sendFlag" :detailChart="detailChart"></detail>
        </el-collapse-item>
      </el-collapse>
    </div>
  </div>
</template>
<script>
import api from '../api';
import Net from './Net.vue';
import Evaluation from './Evaluation.vue';
import Detail from './Detail.vue';

export default {
  name: 'SearchPatents',
  components: {
    Net,
    Evaluation,
    Detail,
  },
  data() {
    // 自定義的變數
    return {
      drugs: [],
      drugDict: [],
      drugName: '',
      predication: {},
      evaluation: {},
      loading1: false,
      laoding2: false,
      activeCollapse: [],
      activeGraph: false,
      activeEval: false,
      classifierTypes: [
        // 0:Logistic Regression，1:Naive Bayes，2:Random，Forest，3:SVM
        {
          value: '0',
          label: 'Logistic Regression',
        },
        {
          value: '1',
          label: 'Naive Bayes',
        },
        {
          value: '2',
          label: 'Random Forest',
        },
        {
          value: '3',
          label: 'SVM',
        },
      ],
      type: '',
      year: '',
      detailChart: [],
      addDetail: {},
      sendFlag: false,
    };
  },
  computed: {
    // 隨時監控return的值有沒有變動

    // loading 1 or 2有改變的話 loading就會重新判斷一次
    loading() {
      // 等兩個api跑完再一起打開所有的東西給大家看
      return this.loading1 && this.loading2;
    },
    readyToSearch() {
      // 每一個輸入格都有值之後，才會允許按下搜尋按鈕
      return !!this.drugName && !!this.year && this.type !== null;
    },
  },
  watch: {
    // 監控值有沒有改變來做接下來的應對
    activeGraph(newVal) {
      // element ui用來定義哪個part需要打開
      // 只要有新的圖片需要展示，在api結束後就會打開所有的collapse
      if (newVal) {
        this.activeCollapse.push('biomedicalGraph');
        this.activeCollapse.push('detailEvaluation');
      }
    },
    // 理論上是在detail api打完之後，把新的值傳進detail裡面
    addDetail(newVal) {
      this.detailChart.push(newVal);
      // console.log(this.detailChart);
    },
  },
  methods: {
    // 自定義的function
    handleDetailChart() {
      // detail 的部分，待完成
      this.sendFlag = true;
    },
    getAllDrug() {
      // 打getAllDrug api，並做預處理，變成element ui希望的樣子
      return api.getAllDrug().then((result) => {
        this.drugs = result;
        this.drugDict = this.drugs.map((o) => {
          const obj = { value: o };
          return obj;
        });
      });
    },
    queryDrug(drug, callback) {
      //根據我們的輸入去篩選哪些資料該被呈現出來
      const results = drug
        ? this.drugDict.filter((item) => {
            return item.value.toLowerCase().indexOf(drug.toLowerCase()) === 0;
          })
        : this.drugDict;

      callback(results);
    },
    getPredicate(searchDrug) {
      // getPredicate 把資料塞進變數後取消loading
      return api
        .getPredicate(searchDrug)
        .then((result) => {
          this.predication = result;
        })
        .catch((error) => {
          console.log(error);
        })
        .finally(() => {
          this.loading1 = false;
        });
    },
    getEval(searchDrug) {
      // getEval 把資料塞進變數後取消loading
      return api
        .getEval(searchDrug)
        .then((result) => {
          this.evaluation = result;
        })
        .catch((error) => {
          console.log(error);
        })
        .finally(() => {
          this.loading2 = false;
        });
    },
    handleSearchClick() {
      // 前面的判定都過的話按鈕就可以按下
      this.loading1 = true;
      this.loading2 = true;
      this.predication = {};
      this.evaluation = {};
      this.activeCollapse = [];
      this.detailChart = [];
      this.sendFlag = false;
      const searchDrug = { drugName: this.drugName, endYear: this.year, classifierType: this.type };
      // console.log(searchDrug);
      this.getPredicate(searchDrug);
      this.getEval(searchDrug);
    },
  },
  mounted() {
    // 頁面剛被render就先打這支api
    this.getAllDrug();
  },
};
</script>
<style lang="stylus" scoped></style>
