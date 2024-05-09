import axios from 'axios';
import qs from 'query-string';
import type { DescData } from '@arco-design/web-vue/es/descriptions/interface';

// MANAGE API

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
  category_id: string;
  time_range: Date[];
  address: string;
  lng: number;
  lat: number;
}

export interface EventTicketsInfoModel {
  tickets: Tickets[];
  document_url?: string;
  image_url?: string;
}

export type originalEventCreationModel = EventBaseInfoModel &
  EventTicketsInfoModel;

export interface EventCreationModel {
  title: string;
  category_id: string;
  start_time: number;
  end_time: number;
  latitude: number;
  longitude: number;
  location_name: string;
  tickets: any[];
  document_url: string;
  image_url: string;
}

export interface ExtraEventRecord {
  id: string;
  publisher: string;
  publish_time: string;
  status: string;
}

export type EventRecord = EventCreationModel & ExtraEventRecord;

export interface EventParams extends Partial<EventRecord> {
  current: number;
  pageSize: number;
}

export interface PolicyListRes {
  list: EventRecord[];
  total: number;
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
export function CreateEventApi(data: EventCreationModel) {
  return axios.post('/api/event/create-event', data);
}

export function listEventSize(params: EventParams) {
  return axios.post<number>('/api/event/list-events-size', {
    params,
    paramsSerializer: (obj: any) => {
      return qs.stringify(obj);
    },
  });
}

export function getEventInfo(uuid: string) {
    return axios.post<EventRecord>(`/api/event/get-event?eventId=${uuid}`);
}

export function listEvent(params: EventParams) {
  return axios.post<EventRecord[]>('/api/event/list-events', {
    params,
    paramsSerializer: (obj: any) => {
      return qs.stringify(obj);
    },
  });
}
