<template>
  <div>
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
        <!-- <template slot="prefix">
          <biomedical-icon icon="search" class="filter-prefix"></biomedical-icon>
        </template> -->
      </el-autocomplete>
    </div>
    <div style="display: flex; justify-content: center">
      <el-collapse style="width: 90%">
        <el-collapse-item class="collapse-item-header">
          <template slot="title">
            <span class="collapse-title">Projects</span>
          </template>
          <div>cdcdcd</div>
        </el-collapse-item>
        <el-collapse-item> njnj </el-collapse-item>
      </el-collapse>
    </div>
  </div>
</template>
<script>
import api from '../api';

export default {
  name: 'SearchPatents',
  data() {
    return {
      drugs: [],
      drugDict: [],
      drugName: '',
    };
  },
  methods: {
    getAllDrug() {
      return api.getAllDrug().then((result) => {
        console.log(result.data);
        this.drug = result.data;
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
    // trans() {
    //   this.drugDict = this.drugs.map((o) => {
    //     const obj = { value: o };
    //     return obj;
    //   });
    // },
    handleSearchClick() {},
  },
  mounted() {
    this.getAllDrug();
    // this.trans();
  },
};
</script>
<style lang="stylus" scoped></style>