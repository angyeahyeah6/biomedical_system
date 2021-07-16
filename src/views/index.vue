<template>
  <div v-loading="loading">
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
    <div style="display: flex; justify-content: center; margin-top: 10px">
      <div style="width: 928px; display: flex; justify-content: flex-end">
        <el-button type="primary" plain icon="el-icon-search" @click="handleSearchClick" :disabled="!readyToSearch">
          Search
        </el-button>
      </div>
    </div>

    <div style="display: flex; justify-content: center; margin-top: 30px">
      <el-collapse style="width: 928px" v-model="activeCollapse">
        <el-collapse-item class="collapse-item-header" name="biomedicalGraph">
          <template slot="title">
            <span class="collapse-title">Biomedical Detaial</span>
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
          ></evaluation>
        </el-collapse-item>
      </el-collapse>
    </div>
  </div>
</template>
<script>
import api from '../api';
import Net from './Net.vue';
import Evaluation from './Evaluation.vue';

export default {
  name: 'SearchPatents',
  components: {
    Net,
    Evaluation,
  },
  data() {
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
        {
          value: '0',
          label: 'a',
        },
        {
          value: '1',
          label: 'b',
        },
      ],
      type: '',
      year: '',
    };
  },
  computed: {
    loading() {
      return this.loading1 && this.loading2;
    },
    readyToSearch() {
      return !!this.drugName && !!this.year && this.type !== null;
    },
  },
  watch: {
    activeGraph(newVal) {
      if (newVal) {
        this.activeCollapse.push('biomedicalGraph');
        this.activeCollapse.push('detailEvaluation');
      }
    },
  },
  methods: {
    getAllDrug() {
      return api.getAllDrug().then((result) => {
        this.drugs = result;
        this.drugDict = this.drugs.map((o) => {
          const obj = { value: o };
          return obj;
        });
      });
    },
    queryDrug(drug, callback) {
      const results = drug
        ? this.drugDict.filter((item) => {
            return item.value.toLowerCase().indexOf(drug.toLowerCase()) === 0;
          })
        : this.drugDict;

      callback(results);
    },
    getPredicate(searchDrug) {
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
      this.loading1 = true;
      this.loading2 = true;
      this.predication = {};
      this.evaluation = {};
      this.activeCollapse = [];
      const searchDrug = { drugName: this.drugName, endYear: this.year, classifierType: this.type };
      // console.log(searchDrug);
      this.getPredicate(searchDrug);
      this.getEval(searchDrug);
    },
  },
  mounted() {
    this.getAllDrug();
  },
};
</script>
<style lang="stylus" scoped></style>
