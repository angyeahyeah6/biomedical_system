<template>
  <div style="width: 100%">
    <v-chart :option="graphOption" ref="myChart" v-loading="loading" />
  </div>
</template>
<script>
export default {
  name: 'Net',
  props: {
    predication: {
      type: Object,
      default: () => {},
    },
    drugName: {
      type: String,
      default: () => '',
    },
  },
  data() {
    return {
      graphOption: {},
      categories: [],
      links: [],
      nodes: [],
      loading: false,
      cNode: [],
      locateX: [],
      locateY: [],
    };
  },
  watch: {
    predication(newval) {
      if (newval) {
        this.categories = [];
        this.links = [];
        this.nodes = [];
        this.cNode = [];
        this.locateX = [];
        this.locateY = [];
        this.optionProcess(newval);
      }
    },
  },
  methods: {
    chainGraphOption() {
      this.loading = true;
      this.graphOption = {
        legend: [
          {
            data: this.categories.map((a) => {
              return a.name;
            }),
          },
        ],
        animationDurationUpdate: 1500,
        animationEasingUpdate: 'quinticInOut',
        series: [
          {
            name: 'Les Miserables',
            type: 'graph',
            layout: 'none',
            data: this.nodes,
            links: this.links,
            categories: this.categories,
            roam: true,
            // force: {
            //   edgeLength: [50, 300],
            // },

            label: {
              position: 'right',
              formatter: '{b}',
            },
            lineStyle: {
              color: 'source',
              width: 2,
              curveness: 0.2,
            },

            // edgeSymbol: ['none', 'arrow'],
            // edgeSymbolSize: 4,
            // roam: true,
            // symbolSize: (value) => {
            //   if (!value) return 5;
            //   return value === 20 ? 40 : 30;
            // },
            // tooltip: {
            //   formatter: ({ data }) => {
            //     return data.name;
            //   },
            // },
          },
        ],
      };
      this.loading = false;
    },
    randomXY(min, max) {
      return Math.floor(Math.random() * max) + min;
    },
    optionProcess(newval) {
      this.categories = [{ name: 'A' }, { name: 'B' }, { name: 'C' }];
      const searchDrug = {
        category: 0,
        id: this.drugName,
        name: this.drugName,
        value: 20,
        symbolSize: 30,
        x: 100,
        y: 2500,
      };
      this.nodes.push(searchDrug);

      Object.entries(newval).forEach((o) => {
        const x_b = Math.floor(Math.random() * 3000) + 1000;
        const y_b = Math.floor(Math.random() * 5000) + 0;
        this.nodes.push({
          category: 1,
          id: o[0],
          name: o[0],
          value: 15,
          x: x_b,
          y: y_b,

          relate: Object.entries(o[1]).reduce(
            (relation, item) => {
              relation.push({
                relationship: `${o[0]}__${item[0]}`,
                predicate: item[1],
              });
              return relation;
            },
            [{ relationship: `${this.drugName}__${o[0]}` }]
          ),
        });
        Object.entries(o[1]).map((c) => {
          if (!this.cNode.includes(c[0])) {
            const y_c = Math.floor(Math.random() * 5000) + 0;
            const x_c = 5000;
            this.cNode.push(c[0]);
            this.nodes.push({
              category: 2,
              id: c[0],
              name: c[0],
              value: 15,
              x: x_c,
              y: y_c,
            });
          }
        });
      });

      this.links = this.nodes.reduce((relation, node) => {
        if (node.id === this.drugName) {
          return relation;
        }
        if (!!node.relate) {
          const tempLink = node.relate.reduce((link, item) => {
            const line = item.relationship.split('__');
            const source = line[0];
            const target = line[1];
            link.push({
              source: source,
              target: target,
              //   label: {
              //       formatter: (data) => {
              //         data
              //       },
              //   }
            });
            return link;
          }, []);
          relation.push(...tempLink);
        }
        return relation;
      }, []);

      // console.log(this.links);
      // console.log(this.nodes);
      // console.log(this.categories);
      this.chainGraphOption();
    },
  },
};
</script>
<style lang="stylus" scoped>
.echarts
  width 928px
  height 75vh
  margin auto
</style>