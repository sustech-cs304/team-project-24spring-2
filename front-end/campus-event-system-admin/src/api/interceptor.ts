import axios from 'axios';
import type { AxiosRequestConfig, AxiosResponse } from 'axios';
import { Message, Modal, Notification } from '@arco-design/web-vue';
import { useUserStore } from '@/store';
import { getToken, clearToken } from '@/utils/auth';
import router from '@/router';

export interface HttpResponse<T = unknown> {
  status: number;
  msg: string;
  code: number;
  data: T;
}

if (import.meta.env.VITE_API_BASE_URL) {
  axios.defaults.baseURL = import.meta.env.VITE_API_BASE_URL;
}

axios.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    const { token, expire } = getToken();

    if (!config.headers) {
      config.headers = {};
    }
    if (token) {
      if (expire && new Date().getTime() > Number(expire)) {
        Notification.error({
          title: '登录过期',
          content: '登录过期，请重新登录',
        });
        useUserStore().logoutCallBack();
        const currentRoute = router.currentRoute.value;
        // setTimeout(() => {
        router.push({
          name: 'login',
          query: {
            ...router.currentRoute.value.query,
            redirect: currentRoute.name as string,
          },
        });
        // }, 1000);
      }

      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);
// add response interceptors
axios.interceptors.response.use(
  (response: AxiosResponse<HttpResponse>) => {
    return response;
  },
  (error) => {
    const { status, statusText, data } = error.response;

    if (status === 401) {
      Message.error({
        content: '未登录或登录失败',
        duration: 3 * 1000,
      });
    } else if (status === 403) {
        Message.error({
            content: '操作权限不足',
            duration: 3 * 1000,
          });
    } else if (status === 500) {
      Message.error({
        content: data || statusText,
        duration: 3 * 1000,
      });
    } else {
      Message.error({
        content: data || statusText,
        duration: 3 * 1000,
      });
    }
    return Promise.reject(error);
  }
);
