<template>
  <el-table :data="tableData" style="width: 100%">
    <el-table-column prop="rank" label="Ranking"> </el-table-column>
    <el-table-column prop="cName" label="Disease"> </el-table-column>
    <el-table-column prop="feature1" label="Feature 1"> </el-table-column>
    <el-table-column prop="feature2" label="Feature 2"> </el-table-column>
  </el-table>
</template>
<script>
export default {
  name: 'Evaluation',
  props: {
    evaluation: {
      type: Object,
      default: () => {},
    },
  },

  data() {
    return {
      top_C: [],
      tableData: [],
    };
  },
  watch: {
    evaluation(newVal) {
      if (newVal) {
        Object.entries(newVal).map((o) => {
          if (+o[1].id < 20) this.top_C.push(o);
        });

        this.tableData = this.top_C.map((o, idx) => {
          return {
            rank: idx + 1,
            cName: o[0],
            feature1: o[1].feature1,
            feature2: o[1].feature2,
          };
        });
      }
      //   console.log(this.tableData);
    },
  },
};
</script>