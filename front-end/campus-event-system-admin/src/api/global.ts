import axios from 'axios';

export function getSettings() {
  return axios.post('/api/global/get-settings');
}

export function getSetting(key: string) {
  return axios.post('/api/global/get-setting', {}, { params: { key } });
}
