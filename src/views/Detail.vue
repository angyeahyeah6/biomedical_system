<template>
  <div style="width: 100%">
    <v-chart :option="graphOption" ref="myChart" v-loading="loading" />
  </div>
</template>
<script>
export default {
  name: 'Detail',
  props: {
    detailChart: {
      type: Array,
      default: () => [],
    },
    sendFlag: {
      type: Boolean,
      default: () => false,
    },
  },
  data() {
    return {
      graphOption: {},
      loading: false,
      categories: [],
      nodes: [],
      links: [],
      relationData: [],
    };
  },
  watch: {
    sendFlag(newVal) {
      if (newVal) {
        this.toGraph(this.detailChart);
      } else {
        this.graphOption = {};
      }
    },
  },
  methods: {
    toGraph(data) {
      this.categories = [{ name: 'B' }, { name: 'C' }];
      data.map((o) => {
        Object.entries(o).reduce((item, relation) => {
          // console.log(item);
          const tmp = {};
          const tmpRelation = [];
          tmp[item[0]] = item[1].reduce((n, predicate) => {
            predicate[n[0]];
          }, {});
        }, []);
      });
    },
  },
};
</script>
<style scoped>
</style>
