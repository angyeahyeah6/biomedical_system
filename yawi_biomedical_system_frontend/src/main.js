import Vue from 'vue';
import App from './App.vue';
import ElementUI from 'element-ui';

import BiomedicalIcon from './components/BiomedicalIcon.vue';
import 'element-ui/lib/theme-chalk/index.css';

import ECharts from 'vue-echarts';

import { use } from 'echarts/core';
import { CanvasRenderer } from 'echarts/renderers';
import { GraphChart } from 'echarts/charts';
import { TitleComponent, TooltipComponent, LegendComponent, DataZoomComponent } from 'echarts/components';

use([CanvasRenderer, GraphChart, TitleComponent, TooltipComponent, LegendComponent, DataZoomComponent]);
Vue.component('v-chart', ECharts);

Vue.component('biomedical-icon', BiomedicalIcon);
Vue.use(ElementUI);

// const isDebug_mode = process.env.NODE_ENV !== 'production';

// isDebug_mode && require('./mock');

Vue.config.productionTip = false;

new Vue({
  render: h => h(App),
}).$mount('#app');
