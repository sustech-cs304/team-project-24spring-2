import axios from 'axios';
import qs from 'query-string';
import type { DescData } from '@arco-design/web-vue/es/descriptions/interface';

// MANAGE API

export interface EventRecord {
  id: string;
  number: number;
  name: string;
  contentType: 'show' | 'callingImg' | 'callingText' | 'socialMeeting';
  count: number;
  capacity: number;
  startTime: string;
  endTime: string;
}

export interface EventParams extends Partial<EventRecord> {
  current: number;
  pageSize: number;
}

export interface PolicyListRes {
  list: EventRecord[];
  total: number;
}

export function queryPolicyList(params: EventParams) {
  return axios.get<PolicyListRes>('/api/event/query', {
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
  eventName: string;
  eventType: string;
  eventTime: string[];
  eventAddress: string;
}
export interface AdvanceInfoModel {
  advertisingSource: string;
  advertisingMedia: string;
  keyword: string[];
  pushNotify: boolean;
  advertisingContent: string;
}

export type UnitChannelModel = BaseInfoModel & AdvanceInfoModel;

export function submiteventForm(data: UnitChannelModel) {
  return axios.post('/api/event/create', { data });
}
