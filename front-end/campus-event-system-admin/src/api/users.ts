import axios from 'axios';
import qs from 'query-string';
import type { DescData } from '@arco-design/web-vue/es/descriptions/interface';

// MANAGE API

export interface UsersRecord {
  id: string;
  number: number;
  name: string;
  contentType: 'show' | 'callingImg' | 'callingText' | 'socialMeeting';
  filterType: 'artificial' | 'rules';
  count: number;
  capacity: number;
  status: 'online' | 'offline';
  startTime: string;
  endTime: string;
}

export interface UsersParams extends Partial<UsersRecord> {
  current: number;
  pageSize: number;
}

export interface PolicyListRes {
  list: UsersRecord[];
  total: number;
}

export function queryUsersList(params: UsersParams) {
  return axios.get<PolicyListRes>('/api/users/query_users', {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

export interface UsersGroup {
  id: number;
  title: string;
  name: string;
  description: string;
  icon?: string;
}


export function queryPermissionGroup() {
  return axios.get('/api/users/permission-group');
}


// CREATE API

export interface BaseInfoModel {
  usersName: string;
  usersType: string;
  usersTime: string[];
  usersAddress: string;
}
export interface AdvanceInfoModel {
  advertisingSource: string;
  advertisingMedia: string;
  keyword: string[];
  pushNotify: boolean;
  advertisingContent: string;
}

export type UnitChannelModel = BaseInfoModel & AdvanceInfoModel;

export function submitusersForm(data: UnitChannelModel) {
  return axios.post('/api/users/create', { data });
}
