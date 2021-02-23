import Vue from "vue";
import App from "./App.vue";
import ElementUI from "element-ui";

import ECharts from "vue-echarts";
// import "echarts/lib/chart/line";
// import "echarts/lib/component/legend";
// import "echarts/lib/component/title.js";
// import "echarts/lib/component/tooltip";
// import "echarts/lib/component/axis";
// import "echarts/lib/chart/pictorialBar.js";
// import "echarts/lib/chart/bar";
// import "echarts/lib/chart/graph";
// import "echarts/lib/chart/scatter";
// import "echarts/lib/chart/sankey";
// import "echarts/lib/component/dataZoom";
Vue.component("v-chart", ECharts);
Vue.use(ElementUI);

const isDebug_mode = process.env.NODE_ENV !== "production";

isDebug_mode && require("./mock");

Vue.config.productionTip = false;

new Vue({
  render: (h) => h(App),
}).$mount("#app");
