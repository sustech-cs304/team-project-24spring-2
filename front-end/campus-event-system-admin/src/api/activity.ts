import axios from 'axios';
import qs from 'query-string';
import type { DescData } from '@arco-design/web-vue/es/descriptions/interface';

// MANAGE API

export interface PolicyRecord {
    id: string;
    number: number;
    name: string;
    contentType: 'show' | 'callingImg' | 'callingText' | 'socialMeeting';
    filterType: 'artificial' | 'rules';
    count: number;
    status: 'online' | 'offline';
    createdTime: string;
}

export interface PolicyParams extends Partial<PolicyRecord> {
    current: number;
    pageSize: number;
}

export interface PolicyListRes {
    list: PolicyRecord[];
    total: number;
}

export function queryPolicyList(params: PolicyParams) {
    return axios.get<PolicyListRes>('/api/activity/query', {
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
    activityName: string;
    activityType: string;
    promotionTime: string[];
    promoteLink: string;
}
export interface AdvanceInfoModel {
    advertisingSource: string;
    advertisingMedia: string;
    keyword: string[];
    pushNotify: boolean;
    advertisingContent: string;
}

export type UnitChannelModel = BaseInfoModel & AdvanceInfoModel;

export function submitActivityForm(data: UnitChannelModel) {
    return axios.post('/api/activity/create', { data });
}
