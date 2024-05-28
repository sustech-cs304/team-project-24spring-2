import axios from 'axios';
import qs from 'query-string';
import type { DescData } from '@arco-design/web-vue/es/descriptions/interface';
import { UserState } from '@/store/modules/user/types';

export interface UsersParams {
  size: number;
  page: number;
  email?: string;
  nickname?: string;
}

export interface UsersRecord {
  id: string;
  nickname: string;
  phone: string;
  email: string;
  avatar_url: string;
  permission_group: string;
}

export function listUsers(params: UsersParams) {
  return axios.post(
    `/api/user/list-user`,
    {},
    {
      params,
    }
  );
}

export function listUsersSize(params: UsersParams) {
  return axios.post(
    `/api/user/list-user-size`,
    {},
    {
      params,
    }
  );
}

export function getUserInfo(uuid: string) {
  return axios.post(
    `/api/user/get-user`,
    {},
    {
      params: {
        userId: uuid,
      },
    }
  );
}

export function changeUserPerm(uuid: string, perm: string) {
  return axios.post<UserState>(
    `/api/user/change-permission`,
    {},
    {
      params: {
        userId: uuid,
        permissionGroup: perm,
      },
    }
  );
}
