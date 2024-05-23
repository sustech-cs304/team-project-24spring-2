import axios from 'axios';
import qs from 'query-string';
import type { DescData } from '@arco-design/web-vue/es/descriptions/interface';
import { UserState } from '@/store/modules/user/types';

export function getUserInfo(uuid: string) {
  return axios.post(
    `/api/user/get-full-user`,
    {},
    {
      params: {
        userId: uuid,
      },
    }
  );
}

export function changeUserPerm(uuid: string) {
    return axios.post<UserState>(
      `/api/get-full-info`,
      {},
      {
        params: {
          userId: uuid,
        },
      }
    );
  }
  
  