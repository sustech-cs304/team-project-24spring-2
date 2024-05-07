import { createApp } from 'vue';
import ArcoVue from '@arco-design/web-vue';
import ArcoVueIcon from '@arco-design/web-vue/es/icon';
import globalComponents from '@/components';
import VueAMap, { initAMapApiLoader } from '@vuemap/vue-amap';
import apikey from '../api_keys.json';
import router from './router';
import store from './store';
import i18n from './locale';
import directive from './directive';
import './mock';
import App from './App.vue';

// Styles are imported via arco-plugin. See config/plugin/arcoStyleImport.ts in the directory for details
// 样式通过 arco-plugin 插件导入。详见目录文件 config/plugin/arcoStyleImport.ts
// https://arco.design/docs/designlab/use-theme-package
import '@/assets/style/global.less';
import '@/api/interceptor';

initAMapApiLoader({
  key: apikey.key,
  securityJsCode: apikey.code, // 新版key需要配合安全密钥使用
  // Loca:{
  //  version: '2.0.0'
  // } // 如果需要使用loca组件库，需要加载Loca
});

const app = createApp(App);

app.use(ArcoVue, {});
app.use(ArcoVueIcon);

app.use(VueAMap);
app.use(router);
app.use(store);
app.use(i18n);
app.use(globalComponents);
app.use(directive);

app.mount('#app');

