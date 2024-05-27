import axios from 'axios';

export interface SettingRecord {
  key: string;
  value: string;
  type?: string;
  range?: string;
  title?: string;
  description?: string;
}

export function getSettings() {
  return axios.post<SettingRecord[]>('/api/global/get-settings');
}

export function getSetting(key: string) {
  return axios.post<string>('/api/global/get-setting', {}, { params: { key } });
}

export function setSetting(key: string, value: string) {
  return axios.post('/api/global/set-setting', {}, { params: { key, value } });
}
