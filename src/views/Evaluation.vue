<template>
  <div v-loading="loading">
    <el-table
      :data="evaluationList"
      style="width: 100%"
      @row-click="handleTableExpansion"
      ref="evaluationTable"
      :default-sort="{ prop: 'rank', order: 'ascending' }"
    >
      <!-- 可以被展開 -->
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
          <div v-show="!!props.row.detail">
            <!-- 加入一個query list -->
            <el-button type="primary" plain icon="el-icon-plus" @click="addDetailChart(props.row.cName)">
              Add Chart
            </el-button>
            <!-- detail按鈕打完pai後把資訊呈現 -->
            <el-table :data="props.row.detail" :default-sort="{ prop: 'importance', order: 'ascending' }">
              <el-table-column prop="relateB" label="Relate B"></el-table-column>
              <el-table-column prop="predicate" label="Predicate"></el-table-column>
              <el-table-column prop="importance" label="Importance"></el-table-column>
            </el-table>
          </div>
        </template>
      </el-table-column>
      <!-- index傳進來的資訊去做處理 -->
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
  // prop是index傳進來的資料
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
    addDetail: {
      type: Object,
      default: () => {},
    },
  },

  data() {
    return {
      top_C: [],
      tableData: [],
      currentPage: 1,
      pagesize: 10,
      curRow: {},
      loading: false,
      detailData: {},
      detailArray: [],
    };
  },
  computed: {
    // table的呈現會根據頁碼去裁減資料，一次顯示幾筆
    evaluationList() {
      return this.tableData.slice((this.currentPage - 1) * this.pagesize, this.currentPage * this.pagesize);
    },
  },
  watch: {
    evaluation(newVal) {
      // 處理成object的樣子
      if (newVal) {
        this.tableData = [];
        this.top_C = [];
        Object.entries(newVal).map((o) => {
          // if (+o[1].id < 20)
          this.top_C.push(o);
        });

        this.tableData = this.top_C.map((o) => {
          return {
            rank: o[1].id + 1,
            cName: o[0],
            feature1: o[1].eval1,
          };
        });
        this.tableData.sort((a, b) => a.rank - b.rank);
      }
      // console.log(this.tableData);
    },
  },
  methods: {
    handleTableExpansion(row) {
      // 看每一列使否被要求展開
      if (row && !row.busy) {
        this.$refs.evaluationTable.toggleRowExpansion(row);
        this.curRow = row;
      }
    },
    addDetailChart(c_name) {
      // 把特別要求的 C 塞進之後要query的的陣列
      // to be done
      const tmp = {};
      tmp[c_name] = this.detailData[c_name];
      this.$emit('update:addDetail', tmp);
      this.$message(`Add ${c_name} to Detail Chart`);
      // console.log(this.detailData[c_name]);
    },
    handleDetailClick(c_name) {
      // 呼叫 detail 的API，後端應該做到砍掉unimportant的predication了
      this.loading = true;
      const searchDetail = {
        drugName: this.drugName,
        endYear: this.year,
        classifierType: this.type,
        c_name: c_name,
      };
      this.handleTableExpansion(this.curRow);
      // console.log(searchDetail);
      return api
        .getDetailPredicate(searchDetail)
        .then((result) => {
          const detail = result.map((o) => {
            return {
              relateB: o[0],
              predicate: o[1],
              importance: o[2] ? 'Important' : 'Unimportant',
            };
          });
          detail.sort((a, b) => (a.importance > b.importance ? -1 : b.importance > a.importance ? 1 : 0));

          this.tableData[this.tableData.findIndex((o) => o.cName === c_name)].detail = detail;
          this.detailData[c_name] = result;
          // console.log(this.tableData[this.tableData.findIndex((o) => o.cName === c_name)]);
        })
        .finally(() => {
          this.handleTableExpansion(this.curRow);
          // console.log(this.detailData);
          this.loading = false;
        });
    },
  },
};
</script>
