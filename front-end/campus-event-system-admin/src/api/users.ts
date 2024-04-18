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

export function queryPolicyList(params: UsersParams) {
  return axios.get<PolicyListRes>('/api/users/query', {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

export interface ServiceRecord {
  id: number;
  title: string;
  description: string;
  name?: string;
  actionType?: string;
  icon?: string;
  data?: DescData[];
  enable?: boolean;
  expires?: boolean;
}

export function queryInspectionList() {
  return axios.get('/api/list/quality-inspection');
}

export function queryTheServiceList() {
  return axios.get('/api/list/the-service');
}

export function queryRulesPresetList() {
  return axios.get('/api/list/rules-preset');
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
