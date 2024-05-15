import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ArcoVue from '@arco-design/web-vue';
import '@arco-design/web-vue/dist/arco.css';

import moment from 'moment';




const app = createApp(App)

app.config.globalProperties.$formatDateTime = function (dateTime) {
    if (typeof dateTime === 'number') {
        dateTime = new Date(dateTime);
    }
    return moment(dateTime).format('YYYY-MM-DD HH:mm:ss');
};

app.use(router)
app.use(ArcoVue);

app.mount('#app')
