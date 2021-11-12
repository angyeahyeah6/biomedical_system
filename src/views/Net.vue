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
      c_num: 0,
      cLink: {},
    };
  },
  watch: {
    predication(newval) {
      if (newval) {
        this.categories = [];
        this.links = [];
        this.nodes = [];
        this.cNode = [];
        this.c_num = 0;
        this.cLink = {};
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
        tooltip: {
          trigger: 'item',
          extraCssText: 'max-width: 600px; word-break: break-word; white-space: normal',
          appendToBody: true,
          confine: true,
        },
        // animationDurationUpdate: 1500,
        // animationEasingUpdate: 'quinticInOut',
        series: [
          {
            name: 'Relationship Graph',
            type: 'graph',
            layout: 'force',
            data: this.nodes,
            links: this.links,
            categories: this.categories,
            roam: true,

            edgeSymbol: ['none', 'arrow'],
            edgeSymbolSize: 4,
            force: {
              repulsion: 500,
              gravity: 0.1,
            },
            emphasis: { focus: 'adjacency' },
            tooltip: {
              extraCssText: 'overflow:scroll',

              formatter: ({ data }) => {
                if (!!data.name) {
                  if (!!data.relateB) {
                    return `<div style: "height: 300px">${data.relateB}<div>`;
                  } else {
                    return data.name;
                  }
                } else {
                  return `${data.source} → ${data.target}`;
                }
              },
            },

            animation: false,
            // lineStyle: {
            //   color: 'source',
            //   width: 2,
            // },
          },
        ],
      };
      this.loading = false;
      if (this.nodes.length !== 1) this.$emit('update:active', true);
      else this.$emit('update:active', false);
    },
    optionProcess(newval) {
      this.categories = [{ name: 'A' }, { name: 'B' }, { name: 'C' }];
      const searchDrug = {
        category: 0,
        id: this.drugName,
        name: this.drugName,
        symbolSize: 10,
      };
      this.nodes.push(searchDrug);
      Object.entries(newval).forEach((o) => {
        this.nodes.push({
          category: 1,
          id: o[0],
          name: o[0],

          symbolSize: 2,
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
            this.cNode.push(c[0]);
            this.nodes.push({
              category: 2,
              id: c[0],
              name: c[0],
              symbolSize: 15,
              relateB: `Disease Name: ${c[0]}<br /> Related B: <br /><span style="margin-left: 30px">${o[0]}</span>`,
            });
            this.c_num += 1;
          } else {
            const idx = this.nodes.findIndex((o) => o.name === c[0]);
            this.nodes[idx].relateB += `<br /><span style="margin-left: 30px">${o[0]}</span>`;
          }
        });
      });
      // console.log(this.nodes);

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
            });
            return link;
          }, []);
          relation.push(...tempLink);
        }
        return relation;
      }, []);

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
