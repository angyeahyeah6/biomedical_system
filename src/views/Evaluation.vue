<template>
  <div>
    <el-table :data="evaluationList" style="width: 100%" @row-click="handleTableExpansion" ref="evaluationTable">
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-button
            v-show="!props.row.detail"
            type="primary"
            plain
            icon="el-icon-search"
            @click="handleDetailClick(props.row.cName)"
          >
            Detail
          </el-button>
          <el-table v-show="!!props.row.detail" :data="props.row.detail">
            <el-table-column prop="relateB" label="Relate B"></el-table-column>
            <el-table-column prop="predicate" label="Predicate"></el-table-column>
            <el-table-column prop="importance" label="Importance"></el-table-column>
          </el-table>
        </template>
      </el-table-column>
      <el-table-column prop="rank" label="Ranking"></el-table-column>
      <el-table-column prop="cName" label="Disease"></el-table-column>
      <el-table-column prop="feature1" label="Feature 1"></el-table-column>
      <!-- <el-table-column prop="feature2" label="Feature 2"> </el-table-column> -->
    </el-table>
    <el-pagination
      :current-page.sync="currentPage"
      :page-size="pagesize"
      :total="top_C.length"
      layout="prev, pager, next"
      style="margin-top: 8px; margin-bottom: 8px"
    ></el-pagination>
  </div>
</template>
<script>
import api from '../api';
export default {
  name: 'Evaluation',
  props: {
    evaluation: {
      type: Object,
      default: () => {},
    },
    drugName: {
      type: String,
      default: () => '',
    },
    year: {
      type: String,
      default: () => '',
    },
    type: {
      type: String,
      default: () => '',
    },
  },

  data() {
    return {
      top_C: [],
      tableData: [],
      currentPage: 1,
      pagesize: 10,
    };
  },
  computed: {
    evaluationList() {
      return this.tableData.slice((this.currentPage - 1) * this.pagesize, this.currentPage * this.pagesize);
    },
  },
  watch: {
    evaluation(newVal) {
      if (newVal) {
        this.tableData = [];
        Object.entries(newVal).map((o) => {
          // if (+o[1].id < 20)
          this.top_C.push(o);
        });

        this.tableData = this.top_C.map((o, idx) => {
          return {
            rank: idx + 1,
            cName: o[0],
            feature1: o[1].eval1,
          };
        });
      }
      // console.log(this.tableData);
    },
  },
  methods: {
    handleTableExpansion(row, col) {
      if (row && !row.busy) {
        this.$refs.evaluationTable.toggleRowExpansion(row);
      }
    },
    handleDetailClick(c_name) {
      const searchDetail = {
        drugName: this.drugName,
        endYear: this.year,
        classifierType: this.type,
        c_name: c_name,
      };
      // console.log(searchDetail);
      return api.getDetailPredicate(searchDetail).then((result) => {
        const detail = result.map((o) => {
          return {
            relateB: o[0],
            predicate: o[1],
            importance: o[2] ? 'Important' : 'Unimportant',
          };
        });
        this.tableData[this.tableData.findIndex((o) => o.cName === c_name)].detail = detail;
        // console.log(this.tableData[this.tableData.findIndex((o) => o.cName === c_name)]);
      });
    },
  },
};
</script>
