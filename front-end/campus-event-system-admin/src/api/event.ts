import axios from 'axios';
import qs from 'query-string';
import type { DescData } from '@arco-design/web-vue/es/descriptions/interface';
import { ref } from 'vue';

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

export function listEvent(params: EventParams) {
  return axios.get<PolicyListRes>('/api/event/list-events', {
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

// CREATE API

export interface Tickets {
  id?: number;
  description: string;
  price: number;
  total_amount: number;
  sold_amount?: number;
}

export interface EventBaseInfoModel {
  title: string;
  category_id: 0;
  time_range: Date[];
  address: string;
}

export interface EventTicketsInfoModel {
  tickets: Tickets[];
  document_url?: string;
  image_url?: string;
}

export type originalEventModel = EventBaseInfoModel & EventTicketsInfoModel;

export interface UnitEventModel {
  title: string;
  category_id: 0;
  start_time: number;
  end_time: number;
  latitude: number;
  longitude: number;
  location_name: string;

  tickets: Tickets[];
  document_url: string;
  image_url: string;
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
export function CreateEventApi(data: UnitEventModel) {
  return axios.post('/api/event/create-event', { data });
}
