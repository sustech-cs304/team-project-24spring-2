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
  category: string;
  time_range: Date[];
  address: string;
  lng: number;
  lat: number;
}

export interface EventTicketsInfoModel {
  tickets: Tickets[];
  document_url?: string;
  image_url?: string;
  uuid?: string;
}

export type originalEventCreationModel = EventBaseInfoModel &
  EventTicketsInfoModel;

export interface EventCreationModel {
  title: string;
  category: string;
  start_time: number;
  end_time: number;
  latitude: number;
  longitude: number;
  location_name: string;
  tickets: any[];
  document_url: string;
  image_url: string;
}

export interface EventUpdateModel {
  title?: string;
  category?: string;
  start_time?: number;
  end_time?: number;
  latitude?: number;
  longitude?: number;
  location_name?: string;
  tickets?: any[];
  document_url?: string;
  image_url?: string;
}

export interface ExtraEventRecord {
  id: string;
  publisher: string;
  publish_time: string;
  status: string;
}

export type EventRecord = EventCreationModel & ExtraEventRecord;

export interface EventParams {
  page: number;
  size: number;
  category?: string;
  publisher?: string;
  statuses?: string;
  title?: string;
}

export interface PolicyListRes {
  list: EventRecord[];
  total: number;
}



export function CreateEventApi(data: EventCreationModel) {
  return axios.post('/api/event/create-event', data);
}

export function listEventSize(params: EventParams) {
  return axios.post<number>('/api/event/list-events-size', {}, { params });
}

export function getEventInfo(uuid: string) {
  return axios.post<EventRecord>(
    '/api/event/get-event',
    {},
    {
      params: {
        eventId: uuid,
      },
    }
  );
}

export function getTicketInfo(uuid: string) {
  return axios.post<Tickets>(
    '/api/ticket/get-ticket',
    {},
    {
      params: {
        ticketId: uuid,
      },
    }
  );
}

export function listEvent(params: EventParams) {
    
  return axios.post<EventRecord[]>(
    '/api/event/list-events',
    {},
    {
      params,
    }
  );
}

export function publishEvent(uuid: string) {
  return axios.post(
    '/api/event/publish-event',
    {},
    {
      params: {
        eventId: uuid,
      },
    }
  );
}
export function updateEvent(uuid: string, data: EventUpdateModel) {
  return axios.post('/api/event/update-event', data, {
    params: {
      eventId: uuid,
    },
  });
}

export function auditEvent(
  uuid: string,
  pass: 'true' | 'false',
  reason: string,
  ) {
  return axios.post('/api/event/audit-event', {
    reason
  }, {
    params: {
      eventId: uuid,
      pass,
    },
  });
}

const integralDigits = 6;
const decimalPlaces = 2;
const symbol = '¥';

const regHandel = (value: any) => {
  let reg = null;
  let gs = null;
  const dIndex = value.toString().indexOf('.');
  // 点开头处理为 0.
  if (dIndex === 0) {
    value = '0.';
  } else {
    // 连续点转为一个点
    const dIndex2 = value.toString().indexOf('..');
    if (dIndex2 !== -1) {
      value = value.replace(/\.\./, '.');
    }
  }
  value = value.replace(/[^0-9.]/g, '');
  const arr = value.split('.');
  if (arr.length === 2 && arr[1] !== '') {
    reg = new RegExp(
      `^(-)*(\\d{0,${integralDigits}})\\d*\\.(\\d{0,${decimalPlaces}}).*$`
    );
    gs = '$1$2.$3';
  } else {
    reg = new RegExp(`^(-)*(\\d{0,${integralDigits}}).*$`);
    if (dIndex !== -1) {
      gs = '$1$2.';
    } else {
      gs = '$1$2';
    }
  }
  return { reg, gs };
};

export function inputNumberF(value: any) {
  const strValue = value.toString();
  const res = regHandel(strValue);
  const val = strValue.replace(res.reg, res.gs);
  return `${symbol} ${val}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}
