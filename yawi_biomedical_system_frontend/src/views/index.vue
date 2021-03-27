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
    <div style="height: 250px; display: flex; align-items: center; justify-content: center">
      <el-autocomplete
        style="width: 928px"
        v-model="drugName"
        :fetch-suggestions="queryDrug"
        placeholder="please input drug name"
      >
        <i class="el-icon-search el-input__icon" slot="suffix" @click="handleSearchClick"> </i>
      </el-autocomplete>
    </div>
    <div style="display: flex; justify-content: center">
      <el-collapse style="width: 928px">
        <el-collapse-item class="collapse-item-header">
          <template slot="title">
            <span class="collapse-title">Projects</span>
          </template>
          <net :predication="predication" :drugName="drugName"></net>
        </el-collapse-item>
        <el-collapse-item> njnj </el-collapse-item>
      </el-collapse>
    </div>
  </div>
</template>
<script>
import api from '../api';
import Net from './Net.vue';

export default {
  name: 'SearchPatents',
  components: {
    Net,
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
    };
  },
  computed: {
    loading() {
      return this.loading1 && this.loading2;
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
      const searchDrug = { drugName: this.drugName };
      this.getPredicate(searchDrug);
      this.getEval(searchDrug);
      // return api
      //   .getPredicate(searchDrug)
      //   .then((result) => {
      //     this.predication = result;
      //   })
      //   .finally(() => {
      //     this.loading = false;
      //   });
    },
  },
  mounted() {
    this.getAllDrug();
  },
};
</script>
<style lang="stylus" scoped></style>